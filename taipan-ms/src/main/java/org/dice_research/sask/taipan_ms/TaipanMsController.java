package org.dice_research.sask.taipan_ms;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * The class provides the REST interface for the microservice.
 * 
 * @author André Sonntag
 *
 */
@RestController
public class TaipanMsController {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	private Logger logger = Logger.getLogger(TaipanMsController.class.getName());

	@RequestMapping("/executeSimple")
	public String executeSimple(String data, String extractor, String targetgraph) {
		TaipanMsService service = new TaipanMsService(restTemplate);
		return "Test";
	}


}
