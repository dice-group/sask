package org.dice_research.sask.database_ms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.log4j.Logger;
import org.dice_research.sask.config.YAMLConfig;
import org.dice_research.sask.database_ms.rdftriples.AutoIndexDTO;
import org.dice_research.sask.database_ms.rdftriples.EndPointParameters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author André Sonntag
 * @author Sepide
 * @author Suganya
 */
public class DbService {

	public Logger logger = Logger.getLogger(DbService.class);
	private final String fusekiHostserver;
	private final int fusekiPort;
	private final RestTemplate restTemplate;
	private final String uri;

	public DbService(RestTemplate restTemplate, YAMLConfig config) {
		this.restTemplate = restTemplate;
		this.fusekiHostserver = config.getHostserver();
		this.fusekiPort = config.getPort();
		this.uri = "http://" + fusekiHostserver + ":" + fusekiPort + "/sask";
	}

	/**
	 * method to store triples inside the default graph
	 * 
	 * @param input
	 *            The triples which are to be stored in the form of TTL.
	 * 
	 * 
	 */
	public void updateGraph(String triples) {
		// upload the resulting model
		Model model = ModelFactory.createDefaultModel();
		InputStream inputstm = new ByteArrayInputStream(triples.getBytes());
		model.read(inputstm, null, "TTL");
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(uri);
		accessor.putModel(model);
		updateAutoIndex();
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
	public void updateNamedGraph(String triples, String graphName) {
		UpdateRequest update = UpdateFactory
				.create("INSERT DATA { graph <http://graph/" + graphName + ">{ " + triples + "}}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, uri + "/update");
		processor.execute();
		updateAutoIndex();

	}

	/**
	 * Method to query the default graph.
	 * 
	 * 
	 * @return The query results in json format
	 * 
	 */
	public String queryDefaultGraph(int limit) {

		String query = "SELECT * { {?s ?p ?o} UNION { GRAPH <default> { ?s ?p ?o } } }" + "LIMIT " + limit;

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory
				.sparqlService(uri+"/query", query)) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();

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
	public String queryGraph(String graphName, int limit) {
		
		String query = "SELECT * WHERE {GRAPH <" + graphName + "> {?s ?p ?o}}" + "LIMIT " + limit;
		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory
				.sparqlService(uri+"/query", query)) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();
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

	public Set<String> getNamedGraphs(String dataSet) {

		Set<String> GraphNames = new HashSet<String>();
		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(uri + "/" + dataSet + "/query",
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
	public String processSparqlQuery(String sparqlQuery) {

		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(uri + "/query", sparqlQuery)) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			String json = b.toString();

			return json;
		}
	}

	/**
	 * Method to update the AutoIndex after data is inserted into DB
	 * 
	 */
	private void updateAutoIndex() {
		String URI = getAutoIndexURI() + "/index/create";
		AutoIndexDTO dto = new AutoIndexDTO();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		EndPointParameters endPointParameters = dto.getEndPointParameters();
		endPointParameters.setUrl(uri + "/query");
		endPointParameters.setEntitySelectQuery("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT DISTINCT ?key1 ?key2 \n" + "WHERE{\n" + "?key1 rdfs:label ?key2 .}");
		endPointParameters.setIsEntityCustomized(true);
		dto.setUseLocalDataSource(true);
		HttpEntity<AutoIndexDTO> entity = new HttpEntity<AutoIndexDTO>(dto, headers);

		try {
			restTemplate.postForObject(URI, entity, String.class);
		} catch (Exception ex) {
			logger.info("failed to update autoindex (" + ex.getMessage() + ")");
		}
	}

	private String getAutoIndexURI() {
		return "http://AUTOINDEX";
	}

}
