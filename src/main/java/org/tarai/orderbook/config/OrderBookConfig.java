package org.tarai.orderbook.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Configuration
@ConfigurationProperties(prefix = "order.book")
public class OrderBookConfig {

    @NotBlank
    private String instrumentId;

    @Min(1)
    @Positive
    private Integer level;

    @Positive
    private Integer maxRetries;

    @NotNull
    private URI wsUrl;
}
