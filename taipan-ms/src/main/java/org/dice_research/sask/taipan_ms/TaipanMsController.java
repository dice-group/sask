package org.dice_research.sask.taipan_ms;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.dice_research.sask.config.YAMLConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
	private YAMLConfig config;

	private RestTemplate restTemplate = new RestTemplate();
	private Logger logger = Logger.getLogger(TaipanMsController.class.getName());
	private TaipanMsService service;

	@PostConstruct
	public void init() {
		service = new TaipanMsService(restTemplate, config);
	}

	@RequestMapping("/identify")
	public String identify(String csvContent) throws IOException {
		this.logger.info("TAIPAN-microservice identify() invoked");
		return service.identifySubjectColumn(csvContent);
	}

	@RequestMapping("/smlmapping")
	public String smlmapping(String csvContent) throws IOException {
		this.logger.info("TAIPAN-microservice smlmapping() invoked");
		return service.generateSMLMapping(csvContent);
	}

	@RequestMapping("/property")
	public String property(String csvContent) throws IOException {
		this.logger.info("TAIPAN-microservice propertyRecommender() invoked");
		return service.propertyRecommender(csvContent);
	}
}
