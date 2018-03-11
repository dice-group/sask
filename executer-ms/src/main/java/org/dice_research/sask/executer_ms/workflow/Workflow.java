package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Represents the whole workflow.
 * 
 * @author Kevin Haack
 *
 */
@JsonDeserialize(using = WorkflowDeserializer.class)
public class Workflow implements Serializable {

	public static final String KEY_LINKS = "links";
	public static final String KEY_OPERATORS = "operators";
	private static final long serialVersionUID = 1L;

	/**
	 * All links in the workflow.
	 */
	private List<Link> links = new ArrayList<>();
	/**
	 * All operators on the workflow.
	 */
	private Map<String, Operator> operators = new HashMap<>();

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public Map<String, Operator> getOperators() {
		return operators;
	}

	public void setOperators(Map<String, Operator> operators) {
		this.operators = operators;
	}
}
