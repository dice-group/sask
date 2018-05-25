package org.dice_research.sask.taipan_ms;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
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

	private RestTemplate restTemplate = new RestTemplate();

	private Logger logger = Logger.getLogger(TaipanMsController.class.getName());

	@RequestMapping("/identify")
	public String executeSimple() throws IOException {
		TaipanMsService service = new TaipanMsService(restTemplate);
		String result = service.identifySubjectColumn();
		return result;
	}

	@RequestMapping("/smlmapping")
	public String executeSimple1() throws IOException {
		TaipanMsService service = new TaipanMsService(restTemplate);
		String result = service.generateSMLMapping();
		return result;
	}

	@RequestMapping("/property")
	public String executeSimple2() throws IOException {
		TaipanMsService service = new TaipanMsService(restTemplate);
		String result = service.propertyRecommender();
		return result;
	}
}
