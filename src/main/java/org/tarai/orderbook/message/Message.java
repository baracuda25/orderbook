package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = SubscribeMessage.class, name = "subscribe"),
        @JsonSubTypes.Type(value = L2SnapshotMessage.class, name = "snapshot"),
        @JsonSubTypes.Type(value = L2UpdateMessage.class, name = "l2update"),
        @JsonSubTypes.Type(value = SubscriptionsMessage.class, name = "subscriptions"),
        @JsonSubTypes.Type(value = ErrorMessage.class, name = "error"),
    }
)
public abstract class Message{
}
