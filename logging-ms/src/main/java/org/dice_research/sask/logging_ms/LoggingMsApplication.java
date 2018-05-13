package org.dice_research.sask.logging_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * This class starts the micro service
 * 
 * @author Sepide tari
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LoggingMsApplication {
//	private static final Logger logger = LoggerFactory.getLogger(LoggingMsApplication.class.getName());
	public static void main(String[] args) {
		SpringApplication.run(LoggingMsApplication.class, args);
//		logger.info("The message is: {}");
//		logger.debug("Debugging...");	
	}
}
