package org.dice_research.sask.webclient.extract;

import java.io.Serializable;

/**
 * A Wrapper for the extraction result.
 * 
 * @author Kevin Haack
 *
 */
public class ExtractionResult implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The extraction result.
	 */
	private String result;

	public ExtractionResult() {
	}

	public ExtractionResult(String result) {
		super();
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
