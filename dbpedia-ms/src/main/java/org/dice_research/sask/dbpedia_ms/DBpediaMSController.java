package org.dice_research.sask.dbpedia_ms;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dice_research.sask.dbpedia_ms.DTO.DBpediaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class provides the REST interface for the microservice.
 * 
 * @author Suganya Kannan, Kevin Haack
 *
 */
@RestController
public class DBpediaMSController {
	/**
	 * The logger.
	 */
	private Logger logger = Logger.getLogger(DBpediaMSController.class.getName());

	@RequestMapping("/extractSimple")
	public String extractSimple(String input) {
		this.logger.info("dbpedia-microservice extract() invoked");

		DBpediaDTO dbpedia = new DBpediaDTO();
		dbpedia.setText(input);
		return extract(dbpedia);
	}

	public String extract(DBpediaDTO dbpedia) {
		logger.info("DBPedia-microservice extract invoked");

		if (null == dbpedia || null == dbpedia.getText() || (dbpedia.getText()
		                                                            .trim()
		                                                            .isEmpty())) {
			throw new IllegalArgumentException("No input");
		}

		DBpediaService gateway = new DBpediaService();
		return gateway.extract(dbpedia);
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("DBPedia-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error("DBPedia-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
