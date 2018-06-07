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
		this.restTemplate = restTemplate;
	}
	// Rest call to get HDFS File List from Repo-ms
	public String makeRestCall() {
		
	  	String uri = "http://REPO-MS/getHdfsStructure?location=repo";
  		String response = this.restTemplate.getForObject(uri, String.class);	
  		logger.info(response);
  		return response;						
	 }
}
