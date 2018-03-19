package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;

/**
 * This class represents a simple workflow.
 * 
 * @author Kevin Haack
 *
 */
public class SimpleWorkflow implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The data operator in the workflow.
	 */
	private String data;
	/**
	 * The extractor in the workflow.
	 */
	private String extractor;
	/**
	 * The target graph in the workflow.
	 */
	private String targetgraph;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExtractor() {
		return extractor;
	}

	public void setExtractor(String extractor) {
		this.extractor = extractor;
	}

	public String getTargetgraph() {
		return targetgraph;
	}

	public void setTargetgraph(String targetgraph) {
		this.targetgraph = targetgraph;
	}
}
