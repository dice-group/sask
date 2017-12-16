package org.dice_research.sask.dbpedia_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DBpediaMSApplication {
	public static void main(String[] args) {
		SpringApplication.run(DBpediaMSApplication.class, args);
	}
}
