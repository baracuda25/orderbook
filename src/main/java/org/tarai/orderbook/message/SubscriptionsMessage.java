package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("subscriptions")
public class SubscriptionsMessage extends Message{
    private List<Channel> channels;
}
