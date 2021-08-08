package org.tarai.orderbook.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tarai.orderbook.message.ErrorMessage;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ServiceErrorException extends RuntimeException {
    @NonNull
    private String errorMessage;
    private String reason;

    public ServiceErrorException(ErrorMessage errorMessage){
        super(String.format("%s. %s",errorMessage.getMessage(), errorMessage.getReason()));
        this.errorMessage = errorMessage.getMessage();
        this.reason = errorMessage.getReason();
    }
}
