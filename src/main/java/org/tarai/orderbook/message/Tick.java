package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class Tick {

    private BigDecimal price;
    private BigDecimal size;

    @JsonValue
    public List<String> value() {
        return Arrays.asList(this.price.toString(), this.size.toString());
    }
}
