package org.dice_research.sask.fox_ms;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * 
 * @author Sepide Tari
 * @author André Sonntag
 *
 */
public class FoxDTO implements Serializable {

	public final static Logger logger = Logger.getLogger(FoxDTO.class);
	private static final long serialVersionUID = 1L;

	private String input = "";
	private String type = "text";
	private String task = "ner";
	private String light = "OFF";
	private String output = "Turle";
	private String lang = "en";

	public FoxDTO() {}
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTask() {
		return task;
	}

	public String getLight() {
		return light;
	}

	@Override
	public String toString() {
		return "FoxDTO [input=" + input + ", type=" + type + ", task=" + task + ", light=" + light + ", output="
				+ output + ", lang=" + lang + "]";
	}

	
}
