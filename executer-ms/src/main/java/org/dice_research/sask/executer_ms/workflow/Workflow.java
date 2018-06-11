package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class represents the whole workflow.
 * 
 * @author Kevin Haack
 *
 */
@JsonDeserialize(using = WorkflowDeserializer.class)
public class Workflow implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The JSON key for the links.
	 */
	public static final String KEY_LINKS = "links";
	/**
	 * The JSON key for the operators..
	 */
	public static final String KEY_OPERATORS = "operators";

	/**
	 * All links in the workflow.
	 */
	private List<Link> links = new ArrayList<>();
	/**
	 * All operators on the workflow.
	 */
	private Map<String, Operator> operators = new HashMap<>();

	public Set<Operator> getNextOperators(Operator op) {

		if (null == op)
			throw new IllegalArgumentException();
		Set<Link> operatorLinks = getLinksWithFrom(op);
		Set<Operator> nextOperators = new HashSet<Operator>();

		for (Link l : operatorLinks) {
			nextOperators.add(this.operators.get(l.getToOperator()));
		}

		return nextOperators;
	}

	public Set<Operator> getStartOperatorSet() {
		Set<Operator> startOperators = new HashSet<>();
		for (Operator o : this.operators.values()) {
			if (o.getInputs().isEmpty()) {
				startOperators.add(o);
			}
		}

		if (startOperators.size() == 0)
			throw new RuntimeException("No queue start found.");
		
		return startOperators;
	}

	private Set<Link> getLinksWithFrom(Operator from) {
		Set<Link> links = new HashSet<>();
		for (Link l : this.links) {
			if (l.getFromOperator().equals(from.getId())) {
				links.add(l);
			}
		}
		return links;
	}

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
