package com.project.milkcollection;

import com.project.milkcollection.common.file.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageConfig.class)
public class MilkcollectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilkcollectionApplication.class, args);
	}

}
