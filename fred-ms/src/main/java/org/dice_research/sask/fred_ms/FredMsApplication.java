package org.dice_research.sask.fred_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @author André Sonntag
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FredMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FredMsApplication.class, args);
	}
}
