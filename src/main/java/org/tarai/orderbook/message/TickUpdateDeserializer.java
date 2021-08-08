package org.tarai.orderbook.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

public class TickUpdateDeserializer extends JsonDeserializer<TickUpdate> {
    @Override
    public TickUpdate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        return TickUpdate.builder()
                .side(Side.valueOf(node.get(0).asText().toUpperCase()))
                .price(new BigDecimal(node.get(1).asText()))
                .size(new BigDecimal(node.get(2).asText()))
                .build();
    }
}
