package org.dice_research.sask.cedric_ms;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class provides the REST interface for the microservice.
 * 
 * @author Kevin Haack
 *
 */
@RestController
public class CedricMsController {

	/**
	 * The logger.
	 */
	private Logger logger = Logger.getLogger(CedricMsController.class.getName());

	/**
	 * Represent a simple version of extraction, without configuration.
	 * 
	 * @param input
	 *            The input to extract.
	 * @return The extraction result.
	 */
	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		CedricMsService service = CedricMsService.getInstance();
		return service.extract(input);
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("FRED-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("FRED-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
