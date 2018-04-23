
package org.dice_research.sask.database_ms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.log4j.Logger;
import org.dice_research.sask.database_ms.RDFTriples.TripleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {

	protected Logger logger = Logger.getLogger(DbController.class);

	// storing the triples(@input) inside default graph
	@RequestMapping(value = "/updateGraph")
	public void updateGraph(@RequestBody String input) {

		logger.info("db-microservice is invoked");

		TripleDTO tripleDTO = new TripleDTO();
		tripleDTO.setTriple(input);

		String string_triples = tripleDTO.getTriple();

		UpdateRequest update = UpdateFactory.create("INSERT DATA { " + string_triples + "}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/sask/update");
		processor.execute();

	}

	// represent all the RDF stored in default graph
	@RequestMapping(value = "/queryGraph")
	public String queryGraph() {

		logger.info("db-microservice is invoked");

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(
				"http://localhost:3030/sask/query", "SELECT * { {?s ?p ?o} UNION { GRAPH ?g { ?s ?p ?o } } }")) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();

			System.out.println(json);

			return json;
		}
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		logger.error("database-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		logger.error("database-microservice RuntimeException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
