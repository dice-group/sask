package org.dice_research.sask.repo_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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
