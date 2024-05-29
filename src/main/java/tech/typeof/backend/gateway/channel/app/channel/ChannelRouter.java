package tech.typeof.backend.gateway.channel.app.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.domain.channel.ChannelConfig;

@Component
@RequiredArgsConstructor
public class ChannelRouter {
    private final ChannelConfig channelConfig;

    public Channel routeToChannel(String bizCode) {
        // TODO 未实现渠道路由
        return channelConfig.getChannels().getFirst();
    }
}
