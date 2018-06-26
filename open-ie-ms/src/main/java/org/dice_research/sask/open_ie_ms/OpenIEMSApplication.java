package org.dice_research.sask.open_ie_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenIEMSApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenIEMSApplication.class, args);
	}
}
