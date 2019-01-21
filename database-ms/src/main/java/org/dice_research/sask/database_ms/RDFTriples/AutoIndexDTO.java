package org.dice_research.sask.database_ms.rdftriples;

public class AutoIndexDTO {
	private EndPointParameters endPointParameters;
	private boolean useLocalDataSource = true;

	public EndPointParameters getEndPointParameters() {
		if (this.endPointParameters == null) {
			endPointParameters = new EndPointParameters();
		}
		return this.endPointParameters;
	}

	public boolean isUseLocalDataSource() {
		return useLocalDataSource;
	}

	public void setUseLocalDataSource(boolean useLocalDataSource) {
		this.useLocalDataSource = useLocalDataSource;
	}

}
