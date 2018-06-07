package org.dice_research.sask.intelligentDataAssistant;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the service function
 * @author Juzer
 *
 */

public class IntelligentDataAssistantService {
	
	private final Logger logger = Logger.getLogger(IntelligentDataAssistantService.class.getName());
	
	private final RestTemplate restTemplate;

	public IntelligentDataAssistantService(RestTemplate restTemplate) {
		logger.info("IntelligentDataAssistantService Object Created");
		this.restTemplate = restTemplate;
	}

	public String makeRestCall() {
		  
	  	String uri = "http://REPO-MS/getHdfsStructure?location=repo";
	  	logger.info("Before Make rest call");
  		String response = this.restTemplate.getForObject(uri, String.class);	
  		logger.info(response);
  		logger.info("After Make rest call");
  		return response;						
	 }
}
