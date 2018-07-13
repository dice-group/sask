package org.dice_research.sask.repo_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This class is responsible to starting the micro service
 * 
 * @author André Sonntag
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class RepoMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(RepoMsApplication.class, args);
	}
}
