package org.dice_research.sask.taipan_ms;

import org.dice_research.sask.config.YAMLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * This class is responsible for launching the microservice.
 * 
 * @author André Sonntag
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TaipanMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaipanMsApplication.class, args);
	}

	@Bean
	public YAMLConfig yamlConfig() {
		return new YAMLConfig();
	}

}
