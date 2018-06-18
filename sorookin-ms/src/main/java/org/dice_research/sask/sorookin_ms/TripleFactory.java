package org.dice_research.sask.sorookin_ms;

import java.util.LinkedList;
import java.util.List;

import org.dice_research.sask.sorookin_ms.sorookin.Edge;
import org.dice_research.sask.sorookin_ms.sorookin.RelationGraph;
import org.dice_research.sask.sorookin_ms.sorookin.Triple;

/**
 * Contains methods to create Triples.
 * 
 * @author Suganya, Kevin Haack
 *
 */
public class TripleFactory {

	

	/**
	 * forming the subject and predicate by choosing the words from the tokens
	 * 
	 * @param graph,
	 *            list of integers representing the nodes. Left nodes represent
	 *            subject and right nodes represent object .
	 * @return the final subject or the object depending on the nodes.
	 */
	private static String createStringFromTokens(RelationGraph graph, List<Integer> list) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			builder.append(graph.getTokens().get(list.get(i)));

			if (i < list.size() - 1) {
				builder.append(" ");
			}
		}

		return builder.toString();
	}

	/**
	 * Creates a list of triples from the passed graph.
	 * 
	 * @param graph
	 * 
	 * @return A list of triples.
	 */

	public static List<Triple> create(RelationGraph graph) {
		List<Triple> list = new LinkedList<>();

		for (Edge edge : graph.getEdgeSet()) {
			Triple tri = new Triple();

			tri.setSubject(createStringFromTokens(graph, edge.getLeft()));
			tri.setPredicate(edge.getLexicalInput());
			tri.setObject(createStringFromTokens(graph, edge.getRight()));
			list.add(tri);
		}

		return list;

	}
}
