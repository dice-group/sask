package org.dice_research.sask.fred_ms;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

	@GetMapping(value = "/extract", consumes=MediaType.APPLICATION_JSON_VALUE)
	public String extract(@RequestBody FredDTO fred){
		logger.info("FRED-microservice extract(FredDTO fred) invoked");
		
		if(fred == null) {
		      throw new NullPointerException("No input");
		}
		
		//String input = "Miles Davis was an american jazz musician.";
		String input = fred.getInput();
		String wfd = fred.getWfd_profile();
		String annotation = fred.getTextannotation();
		String format = fred.getFormat();

		//URI creating
		String host = "http://wit.istc.cnr.it/stlab-tools/fred";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(host)
				.queryParam("text", input)
				.queryParam("wfd_profile", wfd)
				.queryParam("textannotation", annotation);

		//Build header for FRED request
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", format);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			
		return result.getBody();
	}
	
	 @ExceptionHandler
	  void handleNullPointerException(NullPointerException e, HttpServletResponse response) throws IOException {
		logger.error("FRED-microservice NullPointerException");
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	  }

}
