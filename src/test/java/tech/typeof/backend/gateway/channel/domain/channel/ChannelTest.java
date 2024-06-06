package tech.typeof.backend.gateway.channel.domain.channel;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChannelTest {

    @SneakyThrows
    @Test
    void getTransportConfig() {
        // 从 JSON 配置文件初始化 ChannelConfig
        var config = ChannelConfig.fromJsonFile("channel/channel-config.json");

        var channelName = "AlvPay";
        var channel = config.getChannels().stream()
                .filter(c -> c.getName().equals(channelName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Channel not found: " + channelName));

        var abilityName = "Payment";
        var httpConfig = channel.getTransportConfig(abilityName);

        assertEquals("api.paymentprovider.com", httpConfig.getHost());
    }
}