package org.tarai.orderbook.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;

public class TickDeserializer extends JsonDeserializer<Tick> {
    @Override
    public Tick deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.readValueAsTree();
        return Tick.builder().price(new BigDecimal(node.get(0).asText())).size(new BigDecimal(node.get(1).asText())).build();
    }
}
