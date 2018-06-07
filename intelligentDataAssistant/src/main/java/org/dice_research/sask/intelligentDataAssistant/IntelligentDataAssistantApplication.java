package org.dice_research.sask.intelligentDataAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for launching the microservice.
 * @author Juzer
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IntelligentDataAssistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntelligentDataAssistantApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
