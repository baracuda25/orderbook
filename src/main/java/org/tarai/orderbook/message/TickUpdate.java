package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper=true)
public class TickUpdate extends Tick{

    private Side side;

    @Override
    @JsonValue
    public List<String> value() {
        LinkedList<String> values = new LinkedList<>(super.value());
        values.addFirst(side.getCode());
        return values;
    }
}
