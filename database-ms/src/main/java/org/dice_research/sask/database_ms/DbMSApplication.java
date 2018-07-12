package org.dice_research.sask.database_ms;

import org.dice_research.sask.config.YAMLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sepide Tari
 * @author Suganya Kannan
 * @author André Sonntag
 **/
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

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
