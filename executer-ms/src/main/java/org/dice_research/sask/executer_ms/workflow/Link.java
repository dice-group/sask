package org.dice_research.sask.executer_ms.workflow;

public class Link {
	
	public static final String KEY_FROM_OPERATOR = "fromOperator";
	public static final String KEY_FROM_CONNECTOR = "fromConnector";
	public static final String KEY_TO_OPERATOR = "toOperator";
	public static final String KEY_TO_CONNECTOR = "toConnector";
	
	private String fromConnector;
	private String fromOperator;
	private String toConnector;
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
