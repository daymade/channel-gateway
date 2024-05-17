package tech.typeof.backend.gateway.channel.infrastructure.codec.format.dynamic;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import tech.typeof.backend.gateway.channel.infrastructure.codec.format.DataFormatter;

import java.util.Map;

public class DynamicDataFormatter implements DataFormatter {
    private Context context = Context.newBuilder("js").allowAllAccess(true).build();

    @Override
    public String format(Object data) {
        context.getBindings("js").putMember("data", data);
        return context.eval("js", "JSON.stringify(data)").asString();
    }

    @Override
    public <T> T parse(String data, Class<T> clazz) {
        context.getBindings("js").putMember("data", data);
        Value result = context.eval("js", "JSON.parse(data)");
        return result.as(clazz);
    }
}
