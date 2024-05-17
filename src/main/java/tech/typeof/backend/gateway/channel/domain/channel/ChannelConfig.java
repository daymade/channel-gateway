package tech.typeof.backend.gateway.channel.domain.channel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class ChannelConfig {
    private List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    // init channel from json when app starts
    @PostConstruct
    public void initChannels() throws IOException {
        channels = ChannelConfig.fromJsonFile("channel/channel-config.json").getChannels();
    }


    public static ChannelConfig fromJsonFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 使用 ClassLoader 从类路径加载资源文件
        try (InputStream inputStream = ChannelConfig.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + filePath);
            }
            return mapper.readValue(inputStream, ChannelConfig.class);
        }
    }
}
