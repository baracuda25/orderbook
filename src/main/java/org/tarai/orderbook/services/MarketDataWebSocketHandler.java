package org.tarai.orderbook.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.tarai.orderbook.config.OrderBookConfig;
import org.tarai.orderbook.exception.ServiceErrorException;
import org.tarai.orderbook.message.Channel;
import org.tarai.orderbook.message.Message;
import org.tarai.orderbook.message.SubscribeMessage;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;

@RequiredArgsConstructor
public class MarketDataWebSocketHandler implements WebSocketHandler {
    private final FluxSink<Message> sink;
    private final OrderBookConfig orderBookConfig;
    private final ObjectMapper objectMapper;

    private String serialize(SubscribeMessage m){
        try {
            return objectMapper.writerFor(Message.class).writeValueAsString(m);
        } catch (JsonProcessingException e) {
            throw  new ServiceErrorException(e.getMessage());
        }
    }

    private Message deserialize(String s){
        try {
            return objectMapper.readValue(s, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new ServiceErrorException(e.getMessage());
        }
    }

    @Override
    public Mono<Void> handle(WebSocketSession s) {
        SubscribeMessage subscribeMessage = new SubscribeMessage(Collections.singletonList(orderBookConfig.getInstrumentId()), Collections.singletonList(Channel.CHANNEL_LEVEL2));
        return Mono.just(subscribeMessage)
                .map(this::serialize)
                .map(s::textMessage)
                .as(s::send)
                .thenMany(s.receive())
                .map(WebSocketMessage::getPayloadAsText)
                .publishOn(Schedulers.single())
                .map(this::deserialize)
                .doOnNext(sink::next)
                .then();
    }
}
