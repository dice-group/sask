package org.dice_research.sask.dbpedia_ms.DTO;

/**
 * The DBPedia data transfer object.
 * 
 * @author Kevin Haack
 *
 */
public class DBpediaDTO {
	private String text = "";
	private float confidence = 0.5f;
	private int support = 0;
	private String spotter = "Default";
	private String disambiguator = "Default";
	private String policy = "whitelist";
	private String types;
	private String sparql;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public String getSpotter() {
		return spotter;
	}

	public void setSpotter(String spotter) {
		this.spotter = spotter;
	}

	public String getDisambiguator() {
		return disambiguator;
	}

	public void setDisambiguator(String disambiguator) {
		this.disambiguator = disambiguator;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getSparql() {
		return sparql;
	}

	public void setSparql(String sparql) {
		this.sparql = sparql;
	}
}
