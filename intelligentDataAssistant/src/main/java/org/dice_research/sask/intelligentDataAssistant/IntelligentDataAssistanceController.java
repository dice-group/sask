package org.dice_research.sask.intelligentDataAssistant;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * The class provides the REST interface for the microservice.
 * @author Juzer
 *
 */
@RestController
public class IntelligentDataAssistanceController {
	
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	private Logger logger = Logger.getLogger(IntelligentDataAssistanceController.class.getName());
	
	@RequestMapping("/executeIntelligentDataAssistant")
	public String executeSimple(String data, String extractor, String targetgraph) {
		logger.info("In IDA Rest call");
		IntelligentDataAssistantService service = new IntelligentDataAssistantService(restTemplate);
		return service.makeRestCall();
	}
	
	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("IntelligentDataAssistance-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("IntelligentDataAssistance-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
