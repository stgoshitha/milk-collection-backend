package com.project.milkcollection.common.file.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.storage")
public record FileStorageConfig(
        String provider,
        String localDirectory,
        long maxFileSize
) {
}