package org.tarai.orderbook.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Channel {
    public static final Channel CHANNEL_HEARTBEAT = new Channel(ChannelName.HEARTBEAT);
    public static final Channel CHANNEL_STATUS = new Channel(ChannelName.STATUS);
    public static final Channel CHANNEL_TICKER = new Channel(ChannelName.TICKER);
    public static final Channel CHANNEL_LEVEL2 = new Channel(ChannelName.LEVEL2);
    public static final Channel CHANNEL_USER = new Channel(ChannelName.USER);
    public static final Channel CHANNEL_MATCHES = new Channel(ChannelName.MATCHES);
    public static final Channel CHANNEL_FULL = new Channel(ChannelName.FULL);

    @NonNull
    private ChannelName name;
    @JsonProperty("product_ids")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] productIds;
}

