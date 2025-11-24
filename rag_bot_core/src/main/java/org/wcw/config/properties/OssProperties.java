package org.wcw.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oss")
@Data
public class OssProperties {
    private String bucketName;
    private String endpoint;
    private String accessKeyId;
    private String secretAccessKeyId;
}
