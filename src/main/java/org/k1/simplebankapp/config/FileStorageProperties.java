package org.k1.simplebankapp.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    @Value("${app.uploadto.cdn}")
    private String uploadDir;
}

