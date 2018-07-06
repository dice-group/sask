package org.dice_research.sask.fox_ms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
	private static StringWriter modelAsString = new StringWriter();

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

		FoxService service = new FoxService();
		return service.extract(fox);
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
		return transformTtl(turtle);
	}

	/**
	 * Transform a simple version of extraction, without configuration.
	 * 
	 * @param input
	 *            The output of the extractor in TTL format.
	 * @return The transformed TTL.
	 */
	private String transformTtl(String extractorOutput) {
		Model model = ModelFactory.createDefaultModel();
		InputStream inputstm = new ByteArrayInputStream(extractorOutput.getBytes());
		model.read(inputstm, null, "TTL");
		String firstModifyQuery = "PREFIX  foxo: <http://www.w3.org/2001/XMLSchema#>\n"
				+ " PREFIX  dbr:  <http://learningsparql.com/ns/addressbook#>\n"
				+ " PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ " PREFIX  its:  <http://www.w3.org/2005/11/its/rdf#>\n"
				+ " PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " PREFIX  nif:  <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#>\n" + " \n"
				+ " CONSTRUCT \n" + "   { \n" + "     ?urilabel rdfs:label ?label .\n" + "   }\n" + " WHERE\n"
				+ "   { ?uri  nif:anchorOf    ?label ;\n" + "           its:taIdentRef  ?urilabel .\n" + "     \n"
				+ "   }\n";

		String secondModifyQuery = "  PREFIX  foxo: <http://www.w3.org/2001/XMLSchema#>\n"
				+ " PREFIX  dbr:  <http://learningsparql.com/ns/addressbook#>\n"
				+ " PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ " PREFIX  its:  <http://www.w3.org/2005/11/its/rdf#>\n"
				+ " PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ " PREFIX  nif:  <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#>\n" + " \n"
				+ " CONSTRUCT \n" + "   { \n" + "     ?s ?p ?o .\n" + "   }\n" + " WHERE\n" + "   { \n"
				+ "     ?x    rdf:subject     ?s ;\n" + "         rdf:predicate   ?p ;\n"
				+ "         rdf:object      ?o.\n" + "   }";

		Query firstQuery = QueryFactory.create(firstModifyQuery);
		QueryExecution firstQexec = QueryExecutionFactory.create(firstQuery, model);
		Model result = firstQexec.execConstruct();

		Query secondQuery = QueryFactory.create(secondModifyQuery);
		QueryExecution secondQexec = QueryExecutionFactory.create(secondQuery, model);
		result.add(secondQexec.execConstruct());
		result.write(modelAsString, "TTL");
		return modelAsString.toString();

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
