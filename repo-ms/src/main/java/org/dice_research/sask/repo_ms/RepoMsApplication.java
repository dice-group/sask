package org.dice_research.sask.repo_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class RepoMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(RepoMsApplication.class, args);
	}
}
