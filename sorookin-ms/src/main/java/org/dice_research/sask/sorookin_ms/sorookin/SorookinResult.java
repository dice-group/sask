package org.dice_research.sask.sorookin_ms.sorookin;

/**
 * The Sorookin result object.
 * 
 * @author Suganya, Kevin Haack
 *
 */
public class SorookinResult {

	/**
	 * The JSON key for the output from the extractor.
	 */
	public static final String KEY_RELATION_GRAPH = "relation_graph";

	
	private RelationGraph relation_graph;

	public RelationGraph getRelation_graph() {
		return relation_graph;
	}

	public void setRelation_graph(RelationGraph relation_graph) {
		this.relation_graph = relation_graph;
	}

	@Override
	public String toString() {
		return "SorookinResult [relation_graph=" + this.relation_graph + "]";
	}
}
