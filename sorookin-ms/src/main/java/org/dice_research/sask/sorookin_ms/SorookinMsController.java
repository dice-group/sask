package org.dice_research.sask.sorookin_ms;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Suganya, Kevin Haack
 *
 */
@RestController
public class SorookinMsController {

	private static final String URI = "http://semanticparsing.ukp.informatik.tu-darmstadt.de:5000/relation-extraction/parse/";
	
	private Logger logger = Logger.getLogger(SorookinMsController.class.getName());

	/**
	 * Represent a simple version of extraction, without configuration.
	 * 
	 * @param input
	 *            The input to extract.
	 * @return The extraction result.
	 */
	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		this.logger.info("Sorookin-microservice extract() invoked");
		SorookinDTO dto = new SorookinDTO();
		dto.setInputtext(input);
		return extract(dto);
	}

	public String extract(SorookinDTO dto) {
		logger.info("Sorookin-microservice extract invoked");
		String input = dto.getInputtext();
		
		if (null == input || input == null || (input.trim().isEmpty())) {
			throw new IllegalArgumentException("No input");
		}

		try {
			logger.info("extract via " + URI);
			logger.info("extract " + input);
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json, text/javascript, */*; q=0.01");
			
			HttpEntity<SorookinDTO> entity = new HttpEntity<SorookinDTO>(dto, headers);
			
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			HttpEntity<SorookinResult> result = restTemplate.postForEntity(URI, entity, SorookinResult.class);
			
			return result.toString();

		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to Sorrokin (" + ex.getMessage() + " " + input + ").", ex);
		}

	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("sorookin-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("sorookin-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
