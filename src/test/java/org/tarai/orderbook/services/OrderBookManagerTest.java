package org.tarai.orderbook.services;

import org.junit.Before;
import org.junit.Test;
import org.tarai.orderbook.config.OrderBookConfig;
import org.tarai.orderbook.message.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class OrderBookManagerTest {

    private OrderBookManager orderBookManager;
    private final static String INSTRUMENT_ID = "BTC-USD";
    public static final L2SnapshotMessage SNAPSHOT_MESSAGE = new L2SnapshotMessage(INSTRUMENT_ID,
            Arrays.asList(
                    tick(BigDecimal.valueOf(111.3), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111.2), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111.1), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111), BigDecimal.valueOf(2))
            ),
            Arrays.asList(
                    tick(BigDecimal.valueOf(111.31), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111.4), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111.6), BigDecimal.valueOf(2)),
                    tick(BigDecimal.valueOf(111.7), BigDecimal.valueOf(2))
            )
    );
    private final static Integer LEVEL = 3;

    @Before
    public void setUp(){
        OrderBookConfig config = new OrderBookConfig(INSTRUMENT_ID,LEVEL,7, URI.create("wss://localhost:8080/ws"));
        orderBookManager = new OrderBookManager(config);
    }

    @Test
    public void handleSnapshotMessageWithBiggerLevel() {
        L2SnapshotMessage snapshotMessage = orderBookManager.handle(SNAPSHOT_MESSAGE);
        assertEquals(snapshotMessage,new L2SnapshotMessage(INSTRUMENT_ID,
                Arrays.asList(
                        tick(BigDecimal.valueOf(111.3), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.2), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.1), BigDecimal.valueOf(2))
                ),
                Arrays.asList(
                        tick(BigDecimal.valueOf(111.31), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.4), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.6), BigDecimal.valueOf(2))
                )
        ));
    }

    @Test
    public void handleUpdates() {
        orderBookManager.handle(SNAPSHOT_MESSAGE);
        L2SnapshotMessage snapshotMessage = orderBookManager.handle(
                new L2UpdateMessage(INSTRUMENT_ID, LocalDateTime.now(),
                        Arrays.asList(
                             tickUpdate(BigDecimal.valueOf(111.3),BigDecimal.ZERO,Side.BUY),
                             tickUpdate(BigDecimal.valueOf(111.25),BigDecimal.valueOf(1),Side.BUY),
                             tickUpdate(BigDecimal.valueOf(111.05),BigDecimal.valueOf(1),Side.BUY),
                             tickUpdate(BigDecimal.valueOf(111.31),BigDecimal.ZERO,Side.SELL),
                             tickUpdate(BigDecimal.valueOf(111.35),BigDecimal.valueOf(1),Side.SELL),
                             tickUpdate(BigDecimal.valueOf(111.7),BigDecimal.valueOf(1),Side.SELL)
                        )
                )
        );
        assertEquals(snapshotMessage,new L2SnapshotMessage(INSTRUMENT_ID,
                Arrays.asList(
                        tick(BigDecimal.valueOf(111.25), BigDecimal.valueOf(1)),
                        tick(BigDecimal.valueOf(111.2), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.1), BigDecimal.valueOf(2))
                ),
                Arrays.asList(
                        tick(BigDecimal.valueOf(111.35), BigDecimal.valueOf(1)),
                        tick(BigDecimal.valueOf(111.4), BigDecimal.valueOf(2)),
                        tick(BigDecimal.valueOf(111.6), BigDecimal.valueOf(2))
                )
        ));
    }

    private static Tick tick(BigDecimal price,BigDecimal size){
        return Tick.builder().price(price).size(size).build();
    }

    private static TickUpdate tickUpdate(BigDecimal price, BigDecimal size, Side side){
        return TickUpdate.builder().price(price).size(size).side(side).build();
    }
}