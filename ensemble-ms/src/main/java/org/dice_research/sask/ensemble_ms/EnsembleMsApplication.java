package org.dice_research.sask.ensemble_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This class starts microservice
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EnsembleMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnsembleMsApplication.class, args);
	}
}
