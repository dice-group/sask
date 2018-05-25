package org.dice_research.sask.sorookin_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@SpringBootApplication()
@EnableDiscoveryClient
public class SorookinMsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SorookinMsApplication.class, args);
	}
	

}
