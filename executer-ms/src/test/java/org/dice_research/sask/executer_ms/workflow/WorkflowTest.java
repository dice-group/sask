package org.dice_research.sask.executer_ms.workflow;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowTest {
	@Test
	public void testCreateQueue() {
		/*
		 * pre
		 */
		System.out.println("test simple queue");
		
		/*
		 * create
		 */
		// o1
		Map<String, String> outputs1 = new HashMap<>();
		outputs1.put("id1_1", "NL");
		
		Operator o1 = new Operator();
		o1.setContent("/text.txt");
		o1.setId("id1");
		o1.setType("file");
		o1.setOutputs(outputs1);
		
		// o2
		Map<String, String> inputs2 = new HashMap<>();
		inputs2.put("id2_1", "NL");
		
		Map<String, String> outputs2 = new HashMap<>();
		outputs2.put("id2_1", "RDF");
		
		Operator o2 = new Operator();
		o2.setContent("FRED-MS");
		o2.setId("id2");
		o2.setType("extractor");
		o2.setInputs(inputs2);
		o2.setOutputs(outputs2);
		
		// o3
		Map<String, String> inputs3 = new HashMap<>();
		inputs3.put("id3_1", "RDF");
		
		Operator o3 = new Operator();
		o3.setContent("sask");
		o3.setId("id3");
		o3.setType("db");
		o3.setInputs(inputs3);
		
		// l1
		Link l1 = new Link();
		l1.setFromOperator("id1");
		l1.setToOperator("id2");
		l1.setFromConnector("id1_1");
		l1.setToConnector("id2_1");
		
		// l2
		Link l2 = new Link();
		l2.setFromOperator("id2");
		l2.setToOperator("id3");
		l2.setFromConnector("id2_1");
		l2.setToConnector("id3_1");
		
		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("id1", o1);
		w.getOperators().put("id2", o2);
		w.getOperators().put("id3", o3);
		w.getLinks().add(l1);
		w.getLinks().add(l2);
		
		/*
		 * test
		 */
		List<Operator> queue = w.createQueue();
		
		assertEquals(3, queue.size());
		assertEquals("id1", queue.get(0).getId());
		assertEquals("id2", queue.get(1).getId());
		assertEquals("id3", queue.get(2).getId());
	}
}
