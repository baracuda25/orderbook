package org.tarai.orderbook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tarai.orderbook.config.OrderBookConfig;
import org.tarai.orderbook.message.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderBookManager {
    private final TreeMap<BigDecimal,BigDecimal> bidsMap = new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<BigDecimal,BigDecimal> asksMap = new TreeMap<>(Comparator.naturalOrder());
    private final Map<Class<?>,MessageHandler> handlers;
    private final static MessageHandler<Message> noOpHandler = new NoOpMessageHandler();
    private final OrderBookConfig orderBookConfig;

    @Autowired
    public OrderBookManager(OrderBookConfig orderBookConfig) {
        this.orderBookConfig = orderBookConfig;
        this.handlers = new HashMap<>();
        handlers.put(L2SnapshotMessage.class,new L2SnapshotMessageHandler());
        handlers.put(L2UpdateMessage.class,new L2UpdateMessageHandler());
    }

    interface MessageHandler<T extends Message> {
        void handle(T message);
    }

    private L2SnapshotMessage buildSnapshot(){
       return new L2SnapshotMessage(orderBookConfig.getInstrumentId(),getTicks(bidsMap),getTicks(asksMap));
    }

    private List<Tick> getTicks(Map<BigDecimal,BigDecimal> ticks) {
        return ticks.entrySet()
                .stream()
                .map(e -> Tick.builder().price(e.getKey()).size(e.getValue()).build()).collect(Collectors.toList());
    }

    class L2SnapshotMessageHandler implements MessageHandler<L2SnapshotMessage>{
        private void processTicks(List<Tick> ticks, Map<BigDecimal,BigDecimal> map){
            map.clear();
            List<Tick> list = ticks.stream().limit(orderBookConfig.getLevel()).collect(Collectors.toList());
            for (Tick tick : list)
                map.put(tick.getPrice(),tick.getSize());
        }

        @Override
        public void handle(L2SnapshotMessage message) {
            processTicks(message.getAsks(),asksMap);
            processTicks(message.getBids(),bidsMap);
        }
    }

    class L2UpdateMessageHandler implements MessageHandler<L2UpdateMessage>{
        @Override
        public void handle(L2UpdateMessage message) {
            message.getChanges().forEach(
               tickUpdate -> {
                   switch (tickUpdate.getSide()){
                       case BUY:
                           processTickUpdate(tickUpdate, bidsMap);
                           break;
                       case SELL:
                           processTickUpdate(tickUpdate, asksMap);
                           break;
                   }
               }
            );
        }

        private void processTickUpdate(TickUpdate tickUpdate, TreeMap<BigDecimal, BigDecimal> bidsMap) {
            if (tickUpdate.getSize().compareTo(BigDecimal.ZERO) == 0) {
                bidsMap.remove(tickUpdate.getPrice());
            } else {
                bidsMap.put(tickUpdate.getPrice(), tickUpdate.getSize());
            }
            if (bidsMap.size() > orderBookConfig.getLevel())
                bidsMap.pollLastEntry();
        }
    }

    static class NoOpMessageHandler implements MessageHandler<Message>{
        @Override
        public void handle(Message message) {
        }
    }

    public <T extends Message> L2SnapshotMessage handle(T m){
        handlers.getOrDefault(m.getClass(),noOpHandler).handle(m);
        return buildSnapshot();
    }

}
