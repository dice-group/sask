package org.dice_research.sask.executer_ms.workflow;

import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorkflowTest {

	@Test
	public void testStartOperatorSet1() {
		Operator o1 = new Operator();
		o1.setType("file");
		o1.setId("node_hgibcp02st");
		o1.setContent("/testData.txt");

		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("node_hgibcp02st", o1);
		
		/*
		 * test
		 */
		Set<Operator> queue = w.getStartOperatorSet();
		assertEquals(1, queue.size());
		assertEquals("node_hgibcp02st", queue.iterator().next().getId());
	}

	@Test
	public void testStartOperatorSet2() {
		Map<String, String> outputs1 = new HashMap<>();
		outputs1.put("output_55uqv2wzcus", "NL");
		outputs1.put("output_y97vayejxre", "NL");

		Operator o1 = new Operator();
		o1.setType("file");
		o1.setId("node_hgibcp02st");
		o1.setContent("/testData.txt");
		o1.setOutputs(outputs1);

		// o2
		Map<String, String> inputs2 = new HashMap<>();
		inputs2.put("input_tzil25otre", "NL");
		inputs2.put("input_2hfqgk71ukp", "NL");

		Map<String, String> outputs2 = new HashMap<>();
		outputs2.put("output_a2b3epd6nd", "RDF");
		outputs2.put("output_09phmd5hdmvi", "RDF");

		Operator o2 = new Operator();
		o2.setType("extractor");
		o2.setId("node_178rj2s179x");
		o2.setContent("FOX-MS");
		o2.setInputs(inputs2);
		o2.setOutputs(outputs2);

		// o3
		Map<String, String> inputs3 = new HashMap<>();
		inputs3.put("input_5ezhp221vou", "RDF");
		inputs3.put("input_g4z7qll7d8s", "RDF");

		Operator o3 = new Operator();
		o3.setType("db");
		o3.setId("node_hcj9pytiiml");
		o3.setContent("sask");
		o3.setInputs(inputs3);

		// l1
		Link l1 = new Link();
		l1.setFromOperator("node_hgibcp02st");
		l1.setToOperator("node_178rj2s179x");

		l1.setFromConnector("output_55uqv2wzcus");
		l1.setToConnector("input_tzil25otre");

		// l2
		Link l2 = new Link();
		l2.setFromOperator("node_178rj2s179x");
		l2.setToOperator("node_hcj9pytiiml");

		l2.setFromConnector("output_a2b3epd6nd");
		l2.setToConnector("input_5ezhp221vou");

		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("node_hgibcp02st", o1);
		w.getOperators().put("node_178rj2s179x", o2);
		w.getOperators().put("node_hcj9pytiiml", o3);
		w.getLinks().add(l1);
		w.getLinks().add(l2);

		/*
		 * test
		 */
		Set<Operator> queue = w.getStartOperatorSet();
		assertEquals(1, queue.size());
		assertEquals("node_hgibcp02st", queue.iterator().next().getId());
	}
	
	@Test
	public void testStartOperatorSet3() {
		Operator o1 = new Operator();
		o1.setType("file");
		o1.setId("node_hgibcp02st");
		o1.setContent("/testData.txt");
		
		Operator o2 = new Operator();
		o2.setType("file");
		o2.setId("node_xxxxxp02st");
		o2.setContent("/testData.txt");

		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("node_hgibcp02st", o1);
		w.getOperators().put("node_xxxxxp02st", o2);
		
		/*
		 * test
		 */
		Set<Operator> queue = w.getStartOperatorSet();
		assertEquals(2, queue.size());
		assertTrue(queue.contains(o1));
		assertTrue(queue.contains(o2));
	}

	@Test
	public void testCreateQueue() {
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
		Set<Operator> queue = w.getStartOperatorSet();
		assertEquals(1, queue.size());
		assertEquals("id1", queue.iterator().next().getId());

		queue = w.getNextOperators(queue.iterator().next());
		assertEquals(1, queue.size());
		assertEquals("id2", queue.iterator().next().getId());

		queue = w.getNextOperators(queue.iterator().next());
		assertEquals(1, queue.size());
		assertEquals("id3", queue.iterator().next().getId());
	}
}
