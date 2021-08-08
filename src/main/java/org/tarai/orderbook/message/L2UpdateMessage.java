package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeName("l2update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class L2UpdateMessage extends Message{
    @JsonProperty("product_id")
    private String productId;
    private LocalDateTime time;
    private List<TickUpdate> changes;
}
