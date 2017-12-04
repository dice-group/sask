package org.dice_research.sask.fred_ms;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author André Sonntag
 *
 */
@RestController
public class FredMsController {
	private Logger logger = Logger.getLogger(FredMsController.class.getName());
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/extract", method = RequestMethod.GET)
	public String extract(FredDTO fred){
		
		logger.info("FRED-microservice extract(FredDTO fred) invoked");
		String input = "Miles Davis was an american jazz musician.";
		//String input = "fred.getInput();"
		String wfd = fred.getWfd_profile();
		String annotation = fred.getTextannotation();
		String format = fred.getFormat();

		String host = "http://wit.istc.cnr.it/stlab-tools/fred";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(host)
				.queryParam("text", input)
				.queryParam("wfd_profile", wfd)
				.queryParam("textannotation", annotation);

		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", format);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			
		return result.getBody();
	}

}
