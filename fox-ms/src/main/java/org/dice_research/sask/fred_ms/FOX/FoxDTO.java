package org.dice_research.sask.fred_ms.FOX;

import java.io.Serializable;

/**
 * Represents a data transfer object for the fox extractor.
 * 
 * @author Sepide Tari
 * @author André Sonntag
 * @author Kevin Haack
 *
 */
public class FoxDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String task = "ner";
	private String type = "text";
	private String input = "";
	private String foxlight = "OFF";
	private int nif = 0;
	private int defaults = 0;
	private String state = "sending";
	private String output = "Turle";
	private String lang = "en";

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getFoxlight() {
		return foxlight;
	}

	public void setFoxlight(String foxlight) {
		this.foxlight = foxlight;
	}

	public int getNif() {
		return nif;
	}

	public void setNif(int nif) {
		this.nif = nif;
	}

	public int getDefaults() {
		return defaults;
	}

	public void setDefaults(int defaults) {
		this.defaults = defaults;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
