package org.dice_research.sask.executer_ms.workflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a operator in the workflow.
 * 
 * @author Kevin Haack
 *
 */
public class Operator implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The JSON key for the properties.
	 */
	public static final String KEY_PROPERTIES = "properties";
	/**
	 * The JSON key for the conent.
	 */
	public static final String KEY_CONTENT = "content";
	/**
	 * The JSON key for the id.
	 */
	public static final String KEY_ID = "id";
	/**
	 * The JSON key for the type.
	 */
	public static final String KEY_TYPE = "type";
	/**
	 * The JSON key for the outputs.
	 */
	public static final String KEY_OUTPUTS = "outputs";
	/**
	 * The JSON key for the inputs.
	 */
	public static final String KEY_INPUTS = "inputs";
	/**
	 * The JSON key for the label.
	 */
	public static final String KEY_LABEL = "label";
	/**
	 * The id of the operator.
	 */
	private String id;
	/**
	 * The conent of the operator.
	 */
	private String content;
	/**
	 * The type of the operator.
	 */
	private String type;
	/**
	 * All inputs.
	 */
	private Map<String, String> inputs = new HashMap<>();
	/**
	 * All outputs.
	 */
	private Map<String, String> outputs = new HashMap<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getInputs() {
		return inputs;
	}

	public void setInputs(Map<String, String> inputs) {
		this.inputs = inputs;
	}

	public Map<String, String> getOutputs() {
		return outputs;
	}

	public void setOutputs(Map<String, String> outputs) {
		this.outputs = outputs;
	}
}
