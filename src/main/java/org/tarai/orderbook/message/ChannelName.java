package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChannelName {
    HEARTBEAT("heartbeat"), STATUS("status"), TICKER("ticker"), LEVEL2("level2"), USER("user"),
    MATCHES("matches"), FULL("full");

    private final String code;

    ChannelName(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
