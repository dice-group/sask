package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;

public class SimpleWorkflow implements Serializable {
	private static final long serialVersionUID = 1L;
	private String data;
	private String extractor;
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
