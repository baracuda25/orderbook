package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("error")
@Data
@NoArgsConstructor
public class ErrorMessage extends Message{
    private String message;
    private String reason;
}
