package org.dice_research.sask.sorookin_ms.relationgraph;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.dice_research.sask.sorookin_ms.SorookinResultParser;
import org.dice_research.sask.sorookin_ms.TripleFactory;
import org.dice_research.sask.sorookin_ms.sorookin.RelationGraph;
import org.dice_research.sask.sorookin_ms.sorookin.SorookinResult;
import org.dice_research.sask.sorookin_ms.sorookin.Triple;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class RelationGraphDeserializerTest {

	@Test(expected = Exception.class)
	public void parseEmptyTest() {

		/*
		 * pre
		 */

		JSONObject jsonObject = new JSONObject();

		SorookinResult graph = SorookinResultParser.parse(jsonObject);

		/*
		 * test
		 */

		assertEquals(0, graph.getRelation_graph().getEdgeSet().size());
		assertEquals(0, graph.getRelation_graph().getTokens().size());
	}

	@Test
	public void parseJsonWithOneTriple() throws JsonProcessingException {
		/*
		 * pre
		 */
		JSONObject obj = new JSONObject();
		JSONArray tokens = new JSONArray();

		tokens.put("subject1");
		tokens.put("predicate1");
		tokens.put("object1");

		obj.put(RelationGraph.KEY_TOKENS, tokens);

		JSONArray left = new JSONArray();
		left.put(0);
		JSONArray right = new JSONArray();
		right.put(2);

		JSONObject edge1 = new JSONObject();
		edge1.put(RelationGraph.KEY_LEFT_NODES, left);
		edge1.put(RelationGraph.KEY_RIGHT_NODES, right);
		edge1.put(RelationGraph.KEY_LEXICAL_INPUT, "predicate1");

		JSONArray edgeSets = new JSONArray();
		edgeSets.put(edge1);

		obj.put(RelationGraph.KEY_EDGES, edgeSets);

		JSONObject relationGraph = new JSONObject();

		relationGraph.put(SorookinResult.KEY_RELATION_GRAPH, obj);

		SorookinResult graph = SorookinResultParser.parse(relationGraph);
		List<Triple> triples = TripleFactory.create(graph.getRelation_graph());

		/*
		 * test
		 */
		assertEquals(1, graph.getRelation_graph().getEdgeSet().size());
		assertEquals(1, triples.size());
		assertEquals("subject1", triples.get(0).getSubject());
		assertEquals("predicate1", triples.get(0).getPredicate());
		assertEquals("object1", triples.get(0).getObject());

	}

	@Test
	public void parseJsonWithTwoTriples() throws JsonProcessingException {
		/*
		 * pre
		 */
		JSONObject edgeSets = new JSONObject();

		JSONArray list1 = new JSONArray();
		list1.put("subject1");
		list1.put("predicate1");
		list1.put("object1");
		list1.put("subject2");
		list1.put("predicate2");
		list1.put("object2");

		edgeSets.put(RelationGraph.KEY_TOKENS, list1);

		JSONArray left_node1 = new JSONArray();
		left_node1.put(0);
		// list2.put(3);
		JSONArray right_node1 = new JSONArray();
		right_node1.put(2);
		// list3.put(5);
		JSONArray left_node2 = new JSONArray();
		left_node2.put(3);
		JSONArray right_node2 = new JSONArray();
		right_node2.put(5);

		JSONObject leftNodes = new JSONObject();
		leftNodes.put(RelationGraph.KEY_LEFT_NODES, left_node1);
		leftNodes.put(RelationGraph.KEY_RIGHT_NODES, right_node1);
		leftNodes.put(RelationGraph.KEY_LEXICAL_INPUT, "predicate1");
		JSONObject rightNodes = new JSONObject();
		rightNodes.put(RelationGraph.KEY_LEFT_NODES, left_node2);
		rightNodes.put(RelationGraph.KEY_RIGHT_NODES, right_node2);
		rightNodes.put(RelationGraph.KEY_LEXICAL_INPUT, "predicate2");

		JSONArray edges = new JSONArray();
		edges.put(leftNodes);
		edges.put(rightNodes);

		edgeSets.put(RelationGraph.KEY_EDGES, edges);

		JSONObject relationGraph = new JSONObject();

		relationGraph.put(SorookinResult.KEY_RELATION_GRAPH, edgeSets);

		SorookinResult graph = SorookinResultParser.parse(relationGraph);
		List<Triple> triples = TripleFactory.create(graph.getRelation_graph());

		/*
		 * test
		 */

		assertEquals(2, graph.getRelation_graph().getEdgeSet().size());
		assertEquals(2, triples.size());
		assertEquals("subject2", triples.get(1).getSubject());
		assertEquals("predicate2", triples.get(1).getPredicate());
		assertEquals("object2", triples.get(1).getObject());

	}

}
