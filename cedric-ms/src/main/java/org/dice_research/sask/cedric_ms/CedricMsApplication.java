package org.dice_research.sask.cedric_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CedricMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CedricMsApplication.class, args);
	}
}
