package org.dice_research.sask.webclient;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dice_research.sask.webclient.extract.ExtractionFacade;
import org.dice_research.sask.webclient.extract.ExtractionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DiscoveryController {
	/**
	 * The logger.
	 */
	private Logger logger = Logger.getLogger(DiscoveryController.class.getName());
	/**
	 * The spring rest template.
	 */
	@Autowired
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping("/extractSimple")
	public ExtractionResult extractSimple(String input) {
		ExtractionFacade extraction = new ExtractionFacade(this.restTemplate);
		return extraction.extract(input);
	}
	
	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("Webclient RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
