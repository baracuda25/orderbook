package org.tarai.orderbook;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tarai.orderbook.exception.ServiceErrorException;
import org.tarai.orderbook.message.*;
import org.tarai.orderbook.services.MarketDataFeedService;
import org.tarai.orderbook.services.OrderBookManager;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private MarketDataFeedService marketDataFeedService;

    @Autowired
    private OrderBookManager orderBookManager;

    private static final String formatString = "| %-2s | %-2s |\n";

    @Bean
    public SimpleModule tickModule() {
        SimpleModule customModule = new SimpleModule("TickModule", new Version(0, 1, 0, null));
        customModule.addDeserializer(Tick.class, new TickDeserializer());
        customModule.addDeserializer(TickUpdate.class, new TickUpdateDeserializer());
        return customModule;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("press Ctrl+C to shutdown application");
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        Disposable subscribe = marketDataFeedService.getStream()
                .filter(message -> !(message instanceof SubscriptionsMessage)) .flatMap(message -> {
                    if(message instanceof ErrorMessage){
                        return Flux.error(new ServiceErrorException(((ErrorMessage) message)));
                    } else {
                        return Mono.just(message);
                    }
                })
                .map(orderBookManager::handle)
                .subscribe(
                        message -> LOGGER.info("\nASKS\n{}\nBIDS\n{}\n", formatTicks(message.getAsks()),formatTicks(message.getBids())),
                        throwable -> {
                            LOGGER.info("Terminating with error:", throwable);
                            shutdownLatch.countDown();
                        }
                );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            subscribe.dispose();
            shutdownLatch.countDown();
        }));
        shutdownLatch.await();
    }

    private static String formatTicks(List<Tick> ticks) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format(formatString,"Price","Size");
        ticks.forEach(a ->formatter.format(formatString,a.getPrice().toString(),a.getSize()));
        return sb.toString();
    }
}