
package org.dice_research.sask.database_ms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.query.QuerySolution;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryExecution;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author Sepide Tari
 * @author Suganya Kannan
 *
 *         Contains all the methods to interact with the fuseki server through
 *         Jena API
 **/

@RestController
public class DbController {

	protected Logger logger = Logger.getLogger(DbController.class);

	/**
	 * method to store triples inside the default graph
	 * 
	 * @param input
	 *            The triples which are to be stored.
	 * 
	 * 
	 */
	@RequestMapping(value = "/updateGraph")

	public void updateGraph(String input) {

		logger.info("db-microservice updateGraph() is invoked");

		TripleDTO tripleDTO = new TripleDTO();
		tripleDTO.setTriple(input);

		String string_triples = tripleDTO.getTriple();

		UpdateRequest update = UpdateFactory.create("INSERT DATA { " + string_triples + "}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/sask/update");
		processor.execute();

	}

	/**
	 * method to store triples inside the named graph If the name of the graph does
	 * not exists it creates a new graph with the given name and stores the triples
	 * in the sask dataset.
	 * 
	 * @param input
	 *            The triples which are to be stored.
	 * 
	 * @param graphName
	 */
	@RequestMapping(value = "/updateNamedGraph")
	public void updateNamedGraph(String input, String graphName) {

		logger.info("db-microservice updateGraph() is invoked");

		TripleDTO tripleDTO = new TripleDTO();
		tripleDTO.setTriple(input);

		String string_triples = tripleDTO.getTriple();

		UpdateRequest update = UpdateFactory
				.create("INSERT DATA { graph <http://graph/" + graphName + ">{ " + string_triples + "}}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/sask/update");
		processor.execute();

	}

	/**
	 * Method to query the default graph.
	 * 
	 * 
	 * @return The query results in json format
	 * 
	 */
	@RequestMapping(value = "/queryDefaultGraph")
	public String queryDefaultGraph() {

		logger.info("db-microservice queryDefaultGraph() is invoked");

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(
				"http://localhost:3030/sask/query", "SELECT * { {?s ?p ?o} UNION { GRAPH <default> { ?s ?p ?o } } }")) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();

			// System.out.println(json);

			return json;
		}
	}

	/**
	 * Method to query the named graph inside the SASK dataset.
	 * 
	 * @param graphName
	 *            The name of the graph
	 * @return The query result in the form of JSON.
	 */
	@RequestMapping(value = "/queryGraph")
	public String queryGraph(String graphName) {

		logger.info("db-microservice queryGraph() is invoked");

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(
				"http://localhost:3030/sask/query", "SELECT * WHERE {GRAPH <" + graphName + "> {?s ?p ?o}}")) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();

			System.out.println(json);

			return json;

		}
	}

	/**
	 * Method to return all the named graphs from a dataset.
	 * 
	 * @param dataSet
	 *            The name of the dataset.
	 * @return all the graph names present in the given dataset.
	 */

	@RequestMapping(value = "/getNamedGraphs")
	public Set<String> getNamedGraphs(String dataSet) {

		logger.info("db-microservice getNamedGraphs() is invoked");
		Set<String> GraphNames = new HashSet<String>();

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(
				"http://localhost:3030/" + dataSet + "/query",
				"SELECT (strafter(str(?g), \"data/\") AS ?GraphName) WHERE { GRAPH ?g { }}")) {
			ResultSet results = qe.execSelect();

			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();

				String gn = soln.get("GraphName").toString();
				GraphNames.add(gn);
			}
			return GraphNames;
		}

	}

	/**
	 * Method to process the given Sparql Query and return the output.
	 * 
	 * @param sparqlQuery
	 *            The query to be processed.
	 * @return The query result in the form of JSON.
	 */
	@RequestMapping(value = "/processSparqlQuery")
	public String processSparqlQuery(String sparqlQuery) {

		logger.info("db-microservice queryGraph() is invoked");

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory
				.sparqlService("http://localhost:3030/sask/query", sparqlQuery)) {
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
