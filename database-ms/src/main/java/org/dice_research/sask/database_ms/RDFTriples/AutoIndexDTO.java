package org.dice_research.sask.database_ms.RDFTriples;

public class AutoIndexDTO {
	private String url = "";

	private String entitySelectQuery = "";
	private boolean useLocalDataSource = true;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEntitySelectQuery() {
		return entitySelectQuery;
	}

	public void setEntitySelectQuery(String entitySelectQuery) {
		this.entitySelectQuery = entitySelectQuery;
	}

	public boolean isUseLocalDataSource() {
		return useLocalDataSource;
	}

	public void setUseLocalDataSource(boolean useLocalDataSource) {
		this.useLocalDataSource = useLocalDataSource;
	}

}
