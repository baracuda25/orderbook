package org.tarai.orderbook.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.tarai.orderbook.config.OrderBookConfig;
import org.tarai.orderbook.exception.ServiceErrorException;
import org.tarai.orderbook.message.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.WebsocketClientSpec;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
@Data
public class MarketDataFeedService {

    private final OrderBookConfig orderBookConfig;
    private final ObjectMapper objectMapper;
    private final ReactorNettyWebSocketClient client;

    private Flux<Message> stream;

    @Autowired
    public MarketDataFeedService(ObjectMapper objectMapper, OrderBookConfig orderBookConfig) {
        this.objectMapper = objectMapper;
        this.orderBookConfig = orderBookConfig;
        this.client =  new ReactorNettyWebSocketClient();
    }

    private void connect(FluxSink<Message> sink) {
        new ReactorNettyWebSocketClient(HttpClient.create(),WebsocketClientSpec.builder().maxFramePayloadLength(Integer.MAX_VALUE))
                .execute(
                        orderBookConfig.getWsUrl(),
                        new MarketDataWebSocketHandler(sink, orderBookConfig,objectMapper)
                )
                .retryWhen(Retry.backoff(orderBookConfig.getMaxRetries(), Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(300)).filter(e-> !(e instanceof ServiceErrorException)))
                .doOnError(sink::error)
                .subscribe();
    }

    @PostConstruct
    public void init() {
        stream = Flux.create(this::connect)
                .publish()
                .autoConnect(1);
    }


}
