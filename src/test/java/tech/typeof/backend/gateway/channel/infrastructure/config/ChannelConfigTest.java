package tech.typeof.backend.gateway.channel.infrastructure.config;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.channel.ChannelConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ChannelConfigTest {

    @SneakyThrows
    @Test
    void fromJsonFile() {
        // 从 JSON 配置文件初始化 ChannelConfig
        ChannelConfig config = ChannelConfig.fromJsonFile("channel/channel-config.json");

        // 输出配置内容
        for (Channel channel : config.getChannels()) {
            System.out.println("Channel Name: " + channel.getName());
            System.out.println("Is Enabled: " + channel.getIsEnabled());
            for (Channel.PaymentAbility ability : channel.getAbilities()) {
                System.out.println("  Ability Name: " + ability.getName());
                System.out.println("  Protocol: " + ability.getProtocol());
                if ("HTTP".equalsIgnoreCase(ability.getProtocol())) {
                    Channel.HTTPConfig httpConfig = ability.getProtocolConfig().getHttpConfig();
                    System.out.println("    Host: " + httpConfig.getHost());
                    System.out.println("    Port: " + httpConfig.getPort());
                    System.out.println("    URL: " + httpConfig.getUrl());
                    System.out.println("    Verb: " + httpConfig.getVerb());
                    System.out.println("    Headers: " + httpConfig.getHeaders());
                    System.out.println("    Query Params: " + httpConfig.getQueryParams());
                    System.out.println("    Body: " + httpConfig.getBodyFormat());
                }
            }
        }

        assertTrue(config.getChannels().getFirst().getIsEnabled());
    }
}