package tech.typeof.backend.gateway.channel.infrastructure.codec.mapping;

import lombok.Data;

@Data
public class MappingRule {
    private String target;
    private String source;
    private boolean required;
    private String defaultValue;
}
