package org.dice_research.sask.executer_ms;

import org.dice_research.sask_commons.workflow.WorkflowDeserializer;
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
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ExecuterMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExecuterMsApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		/*
		 * register deserializer for workflows.
		 * https://www.leveluplunch.com/java/tutorials/033-custom-jackson-date-
		 * deserializer/
		 */
		ObjectMapper objectMapper = new ObjectMapper();

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Object.class, new WorkflowDeserializer());
		objectMapper.registerModule(simpleModule);

		return objectMapper;
	}
}
