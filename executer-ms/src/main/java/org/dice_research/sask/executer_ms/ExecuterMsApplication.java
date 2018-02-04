package org.dice_research.sask.executer_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExecuterMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExecuterMsApplication.class, args);
	}
}
