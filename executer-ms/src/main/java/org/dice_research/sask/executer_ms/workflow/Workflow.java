package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	/**
	 * Create a queue from the workflow.
	 * 
	 * @return
	 */
	public List<Operator> createQueue() {
		LinkedList<Operator> queue = new LinkedList<>();

		Operator start = getStart();

		if (null == start) {
			throw new RuntimeException("No queue start found.");
		}

		queue.add(start);
		addNext(queue);

		return queue;
	}

	/**
	 * Returns the next operator in the queue.
	 */
	private void addNext(LinkedList<Operator> queue) {
		Operator prev = queue.getLast();

		if (prev.getOutputs()
		        .isEmpty()) {
			return;
		}

		if (prev.getOutputs()
		        .size() > 1) {
			throw new RuntimeException("More then one output not supported");
		}

		Link link = getLinkWithFrom(prev);

		if (null == link) {
			throw new RuntimeException("Link with the fromOperator '" + prev.getId() + "' not found.");
		}

		Operator next = this.operators.get(link.getToOperator());

		if (null == next) {
			throw new RuntimeException("Next operator (" + link.getToOperator() + ") not found.");
		}

		if (next.getInputs()
		        .isEmpty()) {
			throw new RuntimeException("Next operator has no inputs.");
		}

		if (next.getInputs()
		        .size() > 1) {
			throw new RuntimeException("More then one inputs not supported.");
		}

		String toKey = next.getInputs()
		                   .keySet()
		                   .iterator()
		                   .next();
		if (!toKey.equals(link.getToConnector())) {
			throw new RuntimeException("The next operator does not have the input '" + toKey + "'");
		}

		queue.add(next);
		addNext(queue);
	}

	/**
	 * Returns the link with the passed from Operator.
	 * 
	 * @param key
	 * @return The found link.
	 */
	private Link getLinkWithFrom(Operator from) {
		Link link = null;

		for (Link l : this.links) {
			if (l.getFromOperator()
			     .equals(from.getId())) {
				if (null != link) {
					throw new RuntimeException("More then one connecting link (" + from.getId() + ") is not supported.");
				}

				link = l;
			}
		}

		return link;
	}

	/**
	 * Returns the start of the queue.
	 * 
	 * @return The first operator in a queue.
	 */
	private Operator getStart() {
		Operator start = null;

		for (Operator o : this.operators.values()) {
			if (o.getInputs()
			     .isEmpty()) {
				if (null != start) {
					throw new RuntimeException("More then one start is not supported.");
				}

				start = o;
			}
		}

		return start;
	}
}
