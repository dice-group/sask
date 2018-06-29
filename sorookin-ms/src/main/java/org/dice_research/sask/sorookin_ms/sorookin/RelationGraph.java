package org.dice_research.sask.sorookin_ms.sorookin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Suganya, Kevin Haack
 *
 */
public class RelationGraph {
	public static final String KEY_TOKENS = "tokens";
	public static final String KEY_EDGES = "edgeSet";
	public static final String KEY_LEFT_NODES = "left";
	public static final String KEY_RIGHT_NODES = "right";
	public static final String KEY_LEXICAL_INPUT = "lexicalInput";

	private List<String> tokens = new ArrayList<>();
	public List<Edge> edgeSet = new LinkedList<>();

	public List<String> getTokens() {
		return tokens;
	}

	public List<Edge> getEdgeSet() {
		return edgeSet;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}

	public void setEdgeSet(List<Edge> edgeSet) {
		this.edgeSet = edgeSet;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("tokens: [");
		for (String t : this.tokens) {
			builder.append("\t" + t);
		}
		builder.append("],");

		builder.append("edgeSet: [");
		for (Edge e : this.edgeSet) {
			builder.append("\t" + e);
		}
		builder.append("]");

		return builder.toString();
	}
}
