package org.dice_research.sask.database_ms;

import java.io.ByteArrayOutputStream;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.dice_research.sask.config.YAMLConfig;

/**
 * 
 * @author André Sonntag
 * @author Sepide
 * @author Suganya
 */
public class DbService {

	private final String fusekiHostserver;
	private final int fusekiPort;
	
	public DbService(YAMLConfig config) {
		this.fusekiHostserver = config.getHostserver();
		this.fusekiPort = config.getPort();
	}
	
	public void updateGraph(String tiples) {
		UpdateRequest update = UpdateFactory.create("INSERT DATA { " + tiples + "}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://"+fusekiHostserver+":"+fusekiPort+"/sask/update");
		processor.execute();
	}

	public String queryGraph() {

		String json = "";
		try (QueryEngineHTTP qe = (QueryEngineHTTP) QueryExecutionFactory.sparqlService(
				"http://"+fusekiHostserver+":"+fusekiPort+"/sask/query", "SELECT * { {?s ?p ?o} UNION { GRAPH ?g { ?s ?p ?o } } }")) {
			ResultSet results = qe.execSelect();
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ResultSetFormatter.outputAsJSON(b, results);
			json = b.toString();
		}
		return json;
	}

}
