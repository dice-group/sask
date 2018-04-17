package org.dice_research.sask.sorookin_ms;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;



/**
 * 
 * @author
 *
 */
@RestController
public class SorookinMsController {

	private Logger logger = Logger.getLogger(SorookinMsController.class.getName());
	private RestTemplate restTemplate = new RestTemplate();


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
		SorookinDTO Sorookin = new SorookinDTO();
		Sorookin.setText(input);
		return extract("Obama was born in usa");	
		}
	public String extract(String Sorookin) {
		logger.info("Sorookin-microservice extract invoked");

		if (Sorookin == null || Sorookin== null || (Sorookin
                .trim()
                .isEmpty())) {
			throw new IllegalArgumentException("No input");
		}
		String uri;
		try {
			//String host="https://soundcloud.com/oembed";
			String host = "http://semanticparsing.ukp.informatik.tu-darmstadt.de:5000/relation-extraction/parse/";
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(host)
			                                                   .queryParam("inputtext", "Obama was born in USA");
			                                                   
//System.out.println(Sorookin.getText());
			uri = builder.build()
			             .toUriString();
			System.out.println(uri);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to build Sorookin uri (" + ex.getMessage() + Sorookin+").", ex);
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Accept", "application/json, text/javascript, */*; q=0.01");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			//ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			HttpEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
			//HttpEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
			//return result.getBody();
			return result.toString()+result.hasBody();

		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to Sorrokin (" + ex.getMessage()+Sorookin + ").", ex);
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
