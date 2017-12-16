package org.dice_research.sask.fred_ms.FOX;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

public class FoxResponse {
    public final static Logger LOG     = Logger.getLogger(FoxResponse.class);
	public static String charset = "UTF-8";

	protected String input = "";
	protected String output = "";
	protected String log = "";

	public String getInput() {
		return decode(input);
	}

	public String getOutput() {
		return decode(output);
	}

	public String getLog() {
		return decode(log);
	}

	private String decode(String s) {
		try {
			return URLDecoder.decode(s, charset);
		} catch (UnsupportedEncodingException e) {
			String error = "Encoding unsupported.";
			LOG.error(error, e);
			throw new RuntimeException(error, e);
		}
	}
}
