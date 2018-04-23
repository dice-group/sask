package org.dice_research.sask.cedric_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This class is responsible for launching the microservice.
 * 
 * @author Kevin Haack
 *
 */
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
@EnableDiscoveryClient
public class CedricMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CedricMsApplication.class, args);
		CedricMsService.getInstance();
	}
}
