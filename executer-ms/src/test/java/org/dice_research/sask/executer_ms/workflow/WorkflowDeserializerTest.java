package org.dice_research.sask.executer_ms.workflow;

import static org.junit.Assert.*;

import java.util.Map;

import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.dice_research.sask_commons.workflow.WorkflowDeserializer;
import org.junit.Test;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WorkflowDeserializerTest {

	@Test
	public void testParse1() {
		/*
		 * pre
		 */
		ObjectNode root = JsonNodeFactory.instance.objectNode();

		/*
		 * create
		 */
		WorkflowDeserializer deserializer = new WorkflowDeserializer();
		Workflow workflow = deserializer.parse(root);

		/*
		 * test
		 */
		assertEquals(0, workflow.getLinks().size());
		assertEquals(0, workflow.getOperators().size());
	}

	@Test
	public void testParse2() {
		/*
		 * pre
		 */
		// link1
		ObjectNode link1 = JsonNodeFactory.instance.objectNode();
		link1.put(Link.KEY_FROM_CONNECTOR, "A");
		link1.put(Link.KEY_TO_CONNECTOR, "B");
		link1.put(Link.KEY_FROM_OPERATOR, "A_a");
		link1.put(Link.KEY_TO_OPERATOR, "B_a");

		// link2
		ObjectNode link2 = JsonNodeFactory.instance.objectNode();
		link2.put(Link.KEY_FROM_CONNECTOR, "C");
		link2.put(Link.KEY_TO_CONNECTOR, "D");
		link2.put(Link.KEY_FROM_OPERATOR, "C_a");
		link2.put(Link.KEY_TO_OPERATOR, "D_a");

		// links
		ArrayNode links = JsonNodeFactory.instance.arrayNode();
		links.add(link1);
		links.add(link2);

		// operators
		ObjectNode operators = JsonNodeFactory.instance.objectNode();

		// root
		ObjectNode root = JsonNodeFactory.instance.objectNode();
		root.set(Workflow.KEY_LINKS, links);
		root.set(Workflow.KEY_OPERATORS, operators);

		/*
		 * create
		 */
		WorkflowDeserializer deserializer = new WorkflowDeserializer();
		Workflow workflow = deserializer.parse(root);

		/*
		 * test
		 */
		// links
		assertEquals(2, workflow.getLinks().size());
		assertEquals("A", workflow.getLinks().get(0).getFromConnector());
		assertEquals("B", workflow.getLinks().get(0).getToConnector());
		assertEquals("A_a", workflow.getLinks().get(0).getFromOperator());
		assertEquals("B_a", workflow.getLinks().get(0).getToOperator());
		assertEquals("C", workflow.getLinks().get(1).getFromConnector());
		assertEquals("D", workflow.getLinks().get(1).getToConnector());
		assertEquals("C_a", workflow.getLinks().get(1).getFromOperator());
		assertEquals("D_a", workflow.getLinks().get(1).getToOperator());

		// operators
		assertEquals(0, workflow.getOperators().size());
	}

	@Test
	public void testParse3() {
		/*
		 * pre
		 */
		// links
		ArrayNode links = JsonNodeFactory.instance.arrayNode();

		// operators
		ObjectNode output1 = JsonNodeFactory.instance.objectNode();
		output1.put(Operator.KEY_LABEL, "NL");

		ObjectNode outputs1 = JsonNodeFactory.instance.objectNode();
		outputs1.set("out1", output1);

		ObjectNode properties1 = JsonNodeFactory.instance.objectNode();
		properties1.put(Operator.KEY_ID, "id1");
		properties1.put(Operator.KEY_CONTENT, "/text.txt");
		properties1.put(Operator.KEY_TYPE, "file");
		properties1.set(Operator.KEY_OUTPUTS, outputs1);

		ObjectNode operator1 = JsonNodeFactory.instance.objectNode();
		operator1.set(Operator.KEY_PROPERTIES, properties1);

		ObjectNode operators = JsonNodeFactory.instance.objectNode();
		operators.set("id1", operator1);

		// root
		ObjectNode root = JsonNodeFactory.instance.objectNode();
		root.set(Workflow.KEY_LINKS, links);
		root.set(Workflow.KEY_OPERATORS, operators);

		/*
		 * create
		 */
		WorkflowDeserializer deserializer = new WorkflowDeserializer();
		Workflow workflow = deserializer.parse(root);

		/*
		 * test
		 */
		// links
		assertEquals(0, workflow.getLinks().size());
		// operators
		Map<String, Operator> os = workflow.getOperators();
		assertEquals(1, os.size());
		assertEquals("id1", os.get("id1").getId());
		assertEquals("/text.txt", os.get("id1").getContent());
		assertEquals("file", os.get("id1").getType());
		assertEquals(1, os.get("id1").getOutputs().size());
		assertEquals("NL", os.get("id1").getOutputs().get("out1"));
	}

}
