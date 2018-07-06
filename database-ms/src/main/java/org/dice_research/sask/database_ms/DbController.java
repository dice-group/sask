
package org.dice_research.sask.database_ms;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dice_research.sask.config.YAMLConfig;
import org.dice_research.sask.database_ms.RDFTriples.TripleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {

	
	@Autowired
	private YAMLConfig config;
	
	public Logger logger = Logger.getLogger(DbController.class);
	private DbService service;
	
	@PostConstruct
	public void init() {
		service = new DbService(config);
	}
	
	// storing the triples(@input) inside default graph
	@RequestMapping(value = "/updateGraph")
	public void updateGraph(@RequestBody String input) {
		logger.info("db-microservice updateGraph() invoked");

		TripleDTO tripleDTO = new TripleDTO();
		tripleDTO.setTriple(input);
		String triple = tripleDTO.getTriple();
		service.updateGraph(triple);
	}

	// represent all the RDF stored in default graph
	@RequestMapping(value = "/queryGraph")
	public String queryGraph() {
		logger.info("db-microservice queryGraph() invoked");
		return service.queryGraph();
	}

	@ExceptionHandler
	public void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		logger.error("database-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	public void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		logger.error("database-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
