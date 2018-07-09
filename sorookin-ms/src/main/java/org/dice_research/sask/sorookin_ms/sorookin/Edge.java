package org.dice_research.sask.sorookin_ms.sorookin;

import java.util.List;

/**
 * Represents relation_graph in form of tokens, subject nodes, object nodes and
 * predicate
 * 
 * @author Suganya, Kevin Haack
 *
 */

public class Edge {
	public static final String KEY_TOKENS = "tokens";
	public static final String KEY_LEFT_NODES = "left";
	public static final String KEY_RIGHT_NODES = "right";
	public static final String KEY_LEXICAL_INPUT = "lexicalInput";

	private List<Integer> left;
	private List<Integer> right;
	private String lexicalInput;

	public List<Integer> getLeft() {
		return left;
	}

	public void setLeft(List<Integer> left) {
		this.left = left;
	}

	public List<Integer> getRight() {
		return right;
	}

	public void setRight(List<Integer> right) {
		this.right = right;
	}

	public String getLexicalInput() {
		return lexicalInput;
	}

	public void setLexicalInput(String lexicalInput) {
		this.lexicalInput = lexicalInput;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("left: [");
		for (Integer i : this.left) {
			builder.append("\t" + i);
		}
		builder.append("],");

		builder.append("right: [");
		for (Integer i : this.right) {
			builder.append("\t" + i);
		}
		builder.append("],");
		builder.append("lexicalInput: " + this.lexicalInput);

		return builder.toString();
	}
}
