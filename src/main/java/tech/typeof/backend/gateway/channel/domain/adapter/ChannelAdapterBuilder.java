package tech.typeof.backend.gateway.channel.domain.adapter;

import org.springframework.stereotype.Component;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.json.JsonDataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http.HttpTransport;

@Component
public class ChannelAdapterBuilder {
    public ChannelAdapter findAdapter(Channel channel) {
        var channelAdapter = new ChannelAdapter()
                .setChannel(channel);

        var ability = channel.getAbilities().getFirst();

        if (ability.getProtocol().equalsIgnoreCase("HTTP")) {
            channelAdapter.setTransport(new HttpTransport());

            var bodyFormat = ability.getProtocolConfig().getHttpConfig().getBodyFormat();
            if (bodyFormat.equalsIgnoreCase("JSON")) {
                channelAdapter.setDataFormatter(new JsonDataFormatter());
            }
        }

        channelAdapter.validate();

        return channelAdapter;
    }
}
