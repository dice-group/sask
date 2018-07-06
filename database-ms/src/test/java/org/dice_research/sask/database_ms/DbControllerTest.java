package org.dice_research.sask.database_ms;

import static org.junit.Assert.assertEquals;

import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import org.junit.Test;

public class DbControllerTest {

	private String string_triples = "<http://scms.eu/tony_hall> <http://dbpedia.org/ontology/president> <http://scms.eu/bbc>.\r\n";

	@Before
	public void createGraph() {

		UpdateRequest update = UpdateFactory
				.create("INSERT DATA { graph <http://localhost:3030/sask/data/graph1> { " + string_triples + "}}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/sask/update");
		processor.execute();
	}

	@Ignore
	@Test
	public void testQueryGraph() throws Exception {

		DbController mockController = new DbController();
		String realOutput = mockController.queryGraph("http://localhost:3030/sask/data/graph1").replace("\n", "\r\n")
				.trim();

		System.out.println(realOutput);

		String expectedOutput = "{\r\n" + "  \"head\": {\r\n" + "    \"vars\": [ \"s\" , \"p\" , \"o\" ]\r\n"
				+ "  } ,\r\n" + "  \"results\": {\r\n" + "    \"bindings\": [\r\n" + "      {\r\n"
				+ "        \"s\": { \"type\": \"uri\" , \"value\": \"http://scms.eu/tony_hall\" } ,\r\n"
				+ "        \"p\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/ontology/president\" } ,\r\n"
				+ "        \"o\": { \"type\": \"uri\" , \"value\": \"http://scms.eu/bbc\" }\r\n" + "      }\r\n"
				+ "    ]\r\n" + "  }\r\n" + "}".trim();

		assertEquals(expectedOutput, realOutput);

	}

	@After
	public void deleteGraph() {
		UpdateRequest update = UpdateFactory
				.create("DELETE DATA { graph <http://localhost:3030/sask/data/graph1> { " + string_triples + "}}");
		UpdateProcessor processor = UpdateExecutionFactory.createRemote(update, "http://localhost:3030/sask/update");
		processor.execute();
	}
}
