package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;

/**
 * Represents a link in the workflow.
 * 
 * @author Kevin Haack
 *
 */
public class Link implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The JSON key for the fromOperator.
	 */
	public static final String KEY_FROM_OPERATOR = "fromOperator";
	/**
	 * The JSON key for the fromConnector.
	 */
	public static final String KEY_FROM_CONNECTOR = "fromConnector";
	/**
	 * The JSON key for the toOperator.
	 */
	public static final String KEY_TO_OPERATOR = "toOperator";
	/**
	 * The JSON key for the toConnector.
	 */
	public static final String KEY_TO_CONNECTOR = "toConnector";
	/**
	 * At which connector starts the link.
	 */
	private String fromConnector;
	/**
	 * At which operator starts the link.
	 */
	private String fromOperator;
	/**
	 * At which connector ends the link.
	 */
	private String toConnector;
	/**
	 * At which operator ends the link.
	 */
	private String toOperator;

	public String getFromConnector() {
		return fromConnector;
	}

	public void setFromConnector(String fromConnector) {
		this.fromConnector = fromConnector;
	}

	public String getFromOperator() {
		return fromOperator;
	}

	public void setFromOperator(String fromOperator) {
		this.fromOperator = fromOperator;
	}

	public String getToConnector() {
		return toConnector;
	}

	public void setToConnector(String toConnector) {
		this.toConnector = toConnector;
	}

	public String getToOperator() {
		return toOperator;
	}

	public void setToOperator(String toOperator) {
		this.toOperator = toOperator;
	}

	@Override
	public String toString() {
		return "Link [" + fromOperator + " (" + fromConnector + "), " + toOperator + " (" + toConnector + ")" + "]";
	}
}
