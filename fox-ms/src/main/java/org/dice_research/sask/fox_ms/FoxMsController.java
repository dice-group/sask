package org.dice_research.sask.fox_ms;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Sepide Tari
 * @author André Sonntag
 * 
 */
@RestController
public class FoxMsController {
	
	private Logger logger = Logger.getLogger(FoxMsController.class.getName());
	private RestTemplate restTemplate = new RestTemplate(); 
	
	@GetMapping(value = "/info")
	public String extract() {
		logger.info("FOX-microservice extract() invoked"); 
		FoxDTO fox = new FoxDTO();
		fox.setInput("The philosopher and mathematician Leibniz was born in Leipzig in 1646 and attended the University of Leipzig from 1661-1666. The current chancellor of Germany, Angela Merkel, also attended this university.");
		String host = "http://fox-demo.aksw.org/fox";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("accept", "application/x-turtle");
		headers.add("Content-Type", "application/json");
		HttpEntity<FoxDTO> request = new HttpEntity<FoxDTO>(fox, headers);
		FoxResponse result = restTemplate.postForObject(host, request, FoxResponse.class);

		return result.input;
	}
	
	
	
	
	@GetMapping(value="/status")
	public String getStatus() {
		return "";
	}
	
}
