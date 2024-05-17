package tech.typeof.backend.gateway.channel.domain.adapter;

import org.junit.jupiter.api.Test;
import tech.typeof.backend.gateway.channel.domain.channel.Channel;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.json.JsonDataFormatter;
import tech.typeof.backend.gateway.channel.infrastructure.transport.protocol.http.HttpTransport;

import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ChannelAdapterBuilderTest {

    @Test
    public void testHttpWithJsonConfig() {
        Channel.PaymentAbility ability = new Channel.PaymentAbility();
        ability.setProtocol("HTTP");

        Channel.HTTPConfig httpConfig = new Channel.HTTPConfig();
        httpConfig.setBodyFormat("JSON");

        var channel = new Channel();

        ChannelAdapterBuilder builder = new ChannelAdapterBuilder();
        ChannelAdapter adapter = builder.findAdapter(channel);

        assertEquals(channel, adapter.getChannel());
        assertTrue(adapter.getTransport() instanceof HttpTransport);
        assertTrue(adapter.getDataFormatter() instanceof JsonDataFormatter);
    }

    @Test
    public void testProtocolNotSupported() {
        Channel.PaymentAbility ability = new Channel.PaymentAbility();
        ability.setProtocol("TCP");

        var channel = new Channel();

        ChannelAdapterBuilder builder = new ChannelAdapterBuilder();

        assertThrows(UnsupportedOperationException.class, () -> builder.findAdapter(channel));
    }

    @Test
    public void testBodyFormatNotSupported() {
        Channel.PaymentAbility ability = new Channel.PaymentAbility();
        ability.setProtocol("HTTP");

        Channel.HTTPConfig httpConfig = new Channel.HTTPConfig();
        httpConfig.setBodyFormat("XML");
//        ability.setProtocolConfig(new Channel.ProtocolConfig().setHttpConfig(httpConfig));
//
//        Channel channel = new Channel().setAbilities(new LinkedList<>(Collections.singletonList(ability)));

        ChannelAdapterBuilder builder = new ChannelAdapterBuilder();

        assertThrows(UnsupportedOperationException.class, () -> builder.findAdapter(channel));
    }
}