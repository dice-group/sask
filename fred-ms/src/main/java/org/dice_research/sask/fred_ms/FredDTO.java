package org.dice_research.sask.fred_ms;

import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * 
 * @author André Sonntag
 * 
 */
public class FredDTO implements Serializable {

	public final static Logger logger = Logger.getLogger(FredDTO.class);
	private static final long serialVersionUID = 1L;

	private String input = "";
	private String prefix = "fred:";
	private String namespace = "http://www.ontologydesignpatterns.org/ont/fred/domain.owl#";
	private boolean wsd = false;
	private boolean wfd = false;
	private String wfd_profile = "b";
	private boolean tense = false;
	private boolean roles = false;
	private String textannotation = "EARMARK";
	private boolean subgraph = false;
	private String format = "text/turtle";

	public FredDTO() {
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getNamespace() {
		return namespace;
	}

	public boolean isWsd() {
		return wsd;
	}

	public boolean isWfd() {
		return wfd;
	}

	public String getWfd_profile() {
		return wfd_profile;
	}

	public boolean isTense() {
		return tense;
	}

	public boolean isRoles() {
		return roles;
	}

	public String getTextannotation() {
		return textannotation;
	}

	public boolean isSubgraph() {
		return subgraph;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return "FredDTO [input=" + input + ", prefix=" + prefix + ", namespace=" + namespace + ", wsd=" + wsd + ", wfd="
				+ wfd + ", wfd_profile=" + wfd_profile + ", tense=" + tense + ", roles=" + roles + ", textannotation="
				+ textannotation + ", subgraph=" + subgraph + ", format=" + format + "]";
	}

}
