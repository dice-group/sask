package org.dice_research.sask.database_ms;

import org.dice_research.sask.config.YAMLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class DbMSApplication {

	public static void main(String[] args) {

		SpringApplication.run(DbMSApplication.class, args);
	}

	@Bean
	YAMLConfig yamlConfig() {
		return new YAMLConfig();
	}

}
