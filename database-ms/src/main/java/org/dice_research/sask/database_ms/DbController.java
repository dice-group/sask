

package org.dice_research.sask.database_ms;

import java.io.IOException;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dice_research.sask.config.YAMLConfig;
import org.dice_research.sask.database_ms.rdftriples.TripleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 
 *
 * @author Sepide Tari
 * @author Suganya Kannan
 *
 *         Contains all the methods to interact with the Fuseki server through
 *         Jena API
 **/

@RestController
public class DbController {
	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	@Autowired
	private YAMLConfig config;

	public Logger logger = Logger.getLogger(DbController.class);
	private DbService service;

	@PostConstruct
	public void init() {
		service = new DbService(restTemplate, config);
	}

	@RequestMapping(value = "/updateGraph")
	public void updateGraph(String input) {
		TripleDTO tripleDTO = new TripleDTO();
		tripleDTO.setTriple(input);
		String triples = tripleDTO.getTriple();
		service.updateGraph(triples);
	}

	/**
	 * method to store triples inside the named graph If the name of the graph does
	 * not exists it creates a new graph with the given name and stores the triples
	 * in the sask dataset. Now it accepts triples in N-Triples format it can be
	 * changed to TTL later.
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
		String triples = tripleDTO.getTriple();
		service.updateNamedGraph(triples, graphName);
	}

	/**
	 * Method to query the default graph.
	 * 
	 * 
	 * @return The query results in json format
	 * 
	 */
	@RequestMapping(value = "/queryDefaultGraph")
	public String queryDefaultGraph(@RequestParam(required = false, defaultValue = "10") int limit) {
		logger.info("db-microservice queryDefaultGraph() is invoked");
		return service.queryDefaultGraph(limit);
	}

	/**
	 * Method to query the named graph inside the SASK dataset.
	 * 
	 * @param graphName
	 *            The name of the graph
	 * @return The query result in the form of JSON.
	 */
	@RequestMapping(value = "/queryGraph")
	public String queryGraph(@RequestParam(required = false, defaultValue = "10") int limit,
			@RequestParam String graphName) {
		logger.info("db-microservice queryGraph() is invoked");
		return service.queryGraph(graphName, limit);
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
		return service.getNamedGraphs(dataSet);
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
		return service.processSparqlQuery(sparqlQuery);
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
