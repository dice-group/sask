package org.dice_research.sask.fox_ms;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.riot.Lang;
import org.apache.log4j.Logger;
import org.dice_research.sask.fox_ms.DTO.FoxDTO;
import org.dice_research.sask_commons.parser.FormatParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Sepide Tari
 * @author André Sonntag
 * @author Kevin Haack
 * 
 */
@RestController
public class FoxMsController {
	/**
	 * The logger.
	 */
	private Logger logger = Logger.getLogger(FoxMsController.class.getName());

	/**
	 * Extract the passed dto.
	 * 
	 * @param fox
	 *            The fox data transfer object.
	 * @return The extraction result.
	 */
	@GetMapping(value = "/extract", produces = "text/turtle", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String extract(@RequestBody FoxDTO fox) {
		this.logger.info("FOX-microservice extract() invoked");

		if (null == fox || null == fox.getInput() || (fox.getInput()
		                                                 .trim()
		                                                 .isEmpty())) {
			throw new IllegalArgumentException("No input");
		}

		FoxService gateway = new FoxService();
		return gateway.extract(fox);
	}

	/**
	 * Represent a simple version of extraction, without configuration.
	 * 
	 * @param input
	 *            The input to extract.
	 * @return The extraction result.
	 */
	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		this.logger.info("FOX-microservice extractSimple() invoked");

		FoxDTO fox = new FoxDTO();
		fox.setInput(input);
		String turtle = extract(fox);
		String extractorOutput = FormatParser.parse(turtle, Lang.TURTLE, Lang.NTRIPLES);
		return extractorOutput;
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("FOX-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("FOX-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
