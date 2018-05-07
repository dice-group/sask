package org.dice_research.sask.taipan_ms;

import org.dice_research.sask.taipan_ms.WorkflowDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This class is responsible for launching the microservice.
 * 
 * @author Kevin Haack
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TaipanMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaipanMsApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}


}
