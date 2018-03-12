package org.dice_research.sask.database_ms.test;

import static org.junit.Assert.assertEquals;

import org.dice_research.sask.database_ms.DbController;
import org.junit.Test;

public class DbControllerTest {

	@Test
	public void testQueryGraph() throws Exception {

		DbController mockController = new DbController();
		String realOutput = mockController.queryGraph().replace("\n","\r\n").trim();

		String expectedOutput ="{\r\n" + 
				"  \"head\": {\r\n" + 
				"    \"vars\": [ \"s\" , \"p\" , \"o\" , \"g\" ]\r\n" + 
				"  } ,\r\n" + 
				"  \"results\": {\r\n" + 
				"    \"bindings\": [\r\n" + 
				"      {\r\n" + 
				"        \"s\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/resource/Barack_Obama\" } ,\r\n" + 
				"        \"p\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/ontology/spouse\" } ,\r\n" + 
				"        \"o\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/resource/Michelle_Obama\" }\r\n" + 
				"      } ,\r\n" + 
				"      {\r\n" + 
				"        \"s\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/resource/Barack_Obama\" } ,\r\n" + 
				"        \"p\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/ontology/placeOfBurial\" } ,\r\n" + 
				"        \"o\": { \"type\": \"uri\" , \"value\": \"http://scms.eu/new_york\" }\r\n" + 
				"      } ,\r\n" + 
				"      {\r\n" + 
				"        \"s\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/resource/Michelle_Obama\" } ,\r\n" + 
				"        \"p\": { \"type\": \"uri\" , \"value\": \"http://dbpedia.org/ontology/placeOfBurial\" } ,\r\n" + 
				"        \"o\": { \"type\": \"uri\" , \"value\": \"http://scms.eu/new_york\" }\r\n" + 
				"      }\r\n" + 
				"    ]\r\n" + 
				"  }\r\n" + 
				"}".trim();
		
		
		assertEquals(expectedOutput, realOutput);
	}

}
