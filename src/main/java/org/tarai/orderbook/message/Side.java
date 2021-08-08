package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Side {

    BUY("buy"),SELL("sell");

    private final String code;

    Side(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
