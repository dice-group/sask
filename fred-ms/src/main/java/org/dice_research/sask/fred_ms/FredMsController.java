package org.dice_research.sask.fred_ms;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dice_research.sask.fred_ms.DTO.FredDTO;
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
 * @author André Sonntag
 *
 */
@RestController
public class FredMsController {
	private Logger logger = Logger.getLogger(FredMsController.class.getName());

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
		FredDTO fred = new FredDTO();
		fred.setInput(input);
		return extract(fred);
	}

	@GetMapping(value = "/extract", produces = "text/rdf+n3", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String extract(@RequestBody FredDTO fred) {
		logger.info("FRED-microservice extract(FredDTO fred) invoked");

		if (null == fred || null == fred.getInput() || (fred.getInput()
		                                                    .trim()
		                                                    .length() == 0)) {
			throw new IllegalArgumentException("No input");
		}

		FredService service = new FredService(restTemplate);
		return service.extract(fred);
	}

	@ExceptionHandler
	public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("FRED-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	public void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("FRED-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
