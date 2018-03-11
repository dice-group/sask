package org.dice_research.sask.executer_ms.workflow;

import java.util.HashMap;
import java.util.Map;

public class Operator {

	public static final String KEY_PROPERTIES = "properties";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_ID = "id";
	public static final String KEY_TYPE = "type";
	public static final String KEY_OUTPUTS = "outputs";
	public static final String KEY_INPUTS = "inputs";
	public static final String KEY_LABEL = "label";

	private String id;
	private String content;
	private String type;
	private Map<String, String> inputs = new HashMap<>();
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
