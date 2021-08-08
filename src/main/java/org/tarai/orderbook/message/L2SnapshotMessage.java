package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonTypeName("snapshot")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class L2SnapshotMessage extends Message {
    @JsonProperty("product_id")
    private String productId;
    private List<Tick> bids = new ArrayList<>();
    private List<Tick> asks = new ArrayList<>();
}
