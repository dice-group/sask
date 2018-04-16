package org.dice_research.sask.fox_ms.DTO;

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

	private int defaults = 0;
	private String foxlight = "OFF";
	private String input = "";
	private String lang = "en";
	private int nif = 0;
	private String output = "Turtle";
	private String state = "sending";
	private String task = "re";
	private String type = "text";

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInput() {
		return this.input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getFoxlight() {
		return this.foxlight;
	}

	public void setFoxlight(String foxlight) {
		this.foxlight = foxlight;
	}

	public int getNif() {
		return this.nif;
	}

	public void setNif(int nif) {
		this.nif = nif;
	}

	public int getDefaults() {
		return this.defaults;
	}

	public void setDefaults(int defaults) {
		this.defaults = defaults;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
