package org.dice_research.sask.webclient;

import java.io.Serializable;

public class Hello implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Hello [message=" + message + "]";
	}

}
