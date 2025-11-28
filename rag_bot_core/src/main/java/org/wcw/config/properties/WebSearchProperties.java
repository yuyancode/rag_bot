package org.wcw.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "search")
@Data
public class WebSearchProperties {
    private String engine;
    private String apiKey;
}
