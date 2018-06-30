package org.dice_research.sask.executer_ms.threading;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ExtractTaskTest {

	@Mock
	RestTemplate restTemplate;

	Operator op = new Operator();
	Workflow wf = null;
	ExtractTask task = null;
	ExecutorService executorService = null;
	Map<String, String> inputs = null;
	Map<String, String> outputs = null;
	ResponseEntity<String> response = null;
	String turtle = "@prefix dbo:   <http://dbpedia.org/ontology/> .\r\n" + 
			"@prefix foxo:  <http://ns.aksw.org/fox/ontology#> .\r\n" + 
			"@prefix schema: <http://schema.org/> .\r\n" + 
			"@prefix oa:    <http://www.w3.org/ns/oa#> .\r\n" + 
			"@prefix foxr:  <http://ns.aksw.org/fox/resource#> .\r\n" + 
			"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\r\n" + 
			"@prefix dbr:   <http://dbpedia.org/resource/> .\r\n" + 
			"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\r\n" + 
			"@prefix its:   <http://www.w3.org/2005/11/its/rdf#> .\r\n" + 
			"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\r\n" + 
			"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\r\n" + 
			"@prefix prov:  <http://www.w3.org/ns/prov#> .\r\n" + 
			"@prefix foaf:  <http://xmlns.com/foaf/0.1/> .\r\n" + 
			"\r\n" + 
			"<http://ns.aksw.org/fox/demo/document-1#char0,5>\r\n" + 
			"        a               nif:CString , nif:Context ;\r\n" + 
			"        nif:beginIndex  \"0\"^^xsd:nonNegativeInteger ;\r\n" + 
			"        nif:endIndex    \"5\"^^xsd:nonNegativeInteger ;\r\n" + 
			"        nif:isString    \"Obama\" .";
	
	@Before
	public void setUp() throws Exception {

		op = new Operator();
		op.setContent("FRED-MS");
		op.setId("id2");
		op.setType("extractor");
		inputs = new HashMap<>();
		outputs = new HashMap<>();
		inputs.put("id2_1", "NL");
		outputs.put("id2_1", "RDF");
		op.setInputs(inputs);
		op.setOutputs(outputs);

		wf = new Workflow();
		wf.getOperators().put("id2", op);

		task = new ExtractTask(restTemplate, wf, op,"Obama");
		executorService = Executors.newSingleThreadExecutor();
		
		response = new ResponseEntity<String>(turtle, HttpStatus.ACCEPTED);

	}

	@Test
	public void test() throws InterruptedException {
		
		Mockito.when(restTemplate.postForEntity(Mockito.startsWith("http://FOX-MS/extractSimple?"), 
												Matchers.any(HttpEntity.class), 
												Matchers.any(Class.class)))
												.thenReturn(response);
		
		
		executorService.submit(task);
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

		assertEquals(turtle,response.getBody());
	}

}
