package org.dice_research.sask.fox_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FoxMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoxMsApplication.class, args);
	}
}
