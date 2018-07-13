package org.dice_research.sask.repo_ms;

import org.dice_research.sask.config.YAMLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * This class is responsible to starting the micro service
 * 
 * @author André Sonntag
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class RepoMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepoMsApplication.class, args);
	}

	@Bean
	YAMLConfig yamlConfig() {
		return new YAMLConfig();
	}


}
