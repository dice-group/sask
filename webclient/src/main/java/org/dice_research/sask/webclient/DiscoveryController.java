package org.dice_research.sask.webclient;

import java.util.List;

import org.apache.log4j.Logger;
import org.dice_research.sask_commons.microservice.Discoverer;
import org.dice_research.sask_commons.microservice.Microservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@RestController
public class DiscoveryController {

	@Autowired
	private DiscoveryClient discoveryClient;

	private Logger logger = Logger.getLogger(DiscoveryController.class.getName());

	@GetMapping(value = "/discoverMicroservices", produces = "application/json")
	public List<Microservice> discoverMicroservices() {
		this.logger.info("WEBCLIENT-microservice discoverMicroservices() invoked");
		Discoverer discoverer = new Discoverer(discoveryClient);
		return discoverer.discover();
	}
}
