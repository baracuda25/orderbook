package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("subscribe")
public class SubscribeMessage extends Message{
    @JsonProperty("product_ids")
    private List<String> productIds;
    private List<Channel> channels;
}
