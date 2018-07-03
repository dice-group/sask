package org.dice_research.sask.database_ms.rdftriples;

public class EndPointParameters {
	private String url = "";
	private Boolean isEntityCustomized = false;
	private String entitySelectQuery = "";

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setIsEntityCustomized(Boolean isEntityCustomized) {
		this.isEntityCustomized = isEntityCustomized;
	}

	public void setEntitySelectQuery(String entitySelectQuery) {
		this.entitySelectQuery = entitySelectQuery;
	}

	public Boolean getIsEntityCustomized() {
		return this.isEntityCustomized;
	}

	public String getEntitySelectQuery() {
		return this.entitySelectQuery;
	}

}