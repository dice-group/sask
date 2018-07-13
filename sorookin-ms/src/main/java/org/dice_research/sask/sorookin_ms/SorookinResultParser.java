package org.dice_research.sask.sorookin_ms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.sask.sorookin_ms.sorookin.Edge;
import org.dice_research.sask.sorookin_ms.sorookin.RelationGraph;
import org.dice_research.sask.sorookin_ms.sorookin.SorookinResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Suganya, Kevin Haack
 *
 */
public class SorookinResultParser {
	
	/**
	 * Method to parse the relation_graph obtained from the Sorookin
	 * 
	 * @param root
	 * 
	 * @return The corresponding java object of the relation graph.
	 */
	public static SorookinResult parse(JSONObject root) {
		SorookinResult result = new SorookinResult();

		try {
			if (root.has(SorookinResult.KEY_RELATION_GRAPH)) {
				JSONObject node = root.getJSONObject(SorookinResult.KEY_RELATION_GRAPH);
				result.setRelation_graph(parseRelationGraph(node));
			}
		} catch (JSONException e) {
			throw new RuntimeException("Failed to parse result.", e);
		}

		return result;
	}

	private static RelationGraph parseRelationGraph(JSONObject node) {
		RelationGraph relationGraph = new RelationGraph();

		relationGraph.setTokens(parseTokens(node.getJSONArray(RelationGraph.KEY_TOKENS)));

		JSONArray jsonEdges = node.getJSONArray(RelationGraph.KEY_EDGES);

		for (int i = 0; i < jsonEdges.length(); i++) {
			relationGraph.getEdgeSet().add(parseEdge(jsonEdges.getJSONObject(i)));
		}

		return relationGraph;
	}

	private static Edge parseEdge(JSONObject o) {
		Edge edge = new Edge();

		edge.setLeft(parseIntArray(o.getJSONArray(RelationGraph.KEY_LEFT_NODES)));
		edge.setRight(parseIntArray(o.getJSONArray(RelationGraph.KEY_RIGHT_NODES)));
		edge.setLexicalInput(o.getString(RelationGraph.KEY_LEXICAL_INPUT));

		return edge;
	}

	private static List<Integer> parseIntArray(JSONArray array) {
		List<Integer> list = new LinkedList<>();

		for (int i = 0; i < array.length(); i++) {
			list.add(array.getInt(i));
		}

		return list;
	}

	private static List<String> parseTokens(JSONArray array) {
		List<String> list = new ArrayList<>();

		for (int i = 0; i < array.length(); i++) {
			list.add(array.getString(i));
		}

		return list;
	}

}
