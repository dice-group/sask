package org.dice_research.sask.webclient.extract;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The facade for the extraction process.
 * 
 * @author Kevin Haack
 *
 */
public class ExtractionFacade {
	/**
	 * The service url from fred.
	 */
	private static final String SERVICE_URL_FRED = "http://FRED-MS/extractSimple";

	/**
	 * The spring restTemplate.
	 */
	private final RestTemplate restTemplate;

	public ExtractionFacade(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Extract the passed text.
	 * 
	 * @param input
	 *            The text to extract.
	 * @return The extraction result.
	 */
	public ExtractionResult extract(String input) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SERVICE_URL_FRED);
			builder = builder.queryParam("input", input);
			
			UriComponents uriComps = builder.build();
			String uri = uriComps.toUriString();
			
			return new ExtractionResult(this.restTemplate.getForObject(uri, String.class));
		} catch (Exception ex) {
			throw new RuntimeException("Failed to exchange data with fred-ms (" + ex.getMessage() + ")", ex);
		}
	}
}
