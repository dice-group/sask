package org.dice_research.sask.webclient.Bootstrap;

import java.io.File;

public class MockSettings {
	public final static String KEY_SUFFIX = "suffix";
	public final static String KEY_DEFAULT_PAGE = "defaultPage";
	public final static String KEY_PAGE_LAYOUT = "layout";
	public final static String KEY_PAGE_FOLDER = "pageFolder";
	public final static String KEY_PAGE_SCRIPT_FOLDER = "pageScriptFolder";
	public final static String KEY_PAGE_SCRIPT_URI = "pageScriptURI";

	public String get(String key) {
		switch (key) {
		case KEY_DEFAULT_PAGE:
			return "index";
		case KEY_PAGE_LAYOUT:
			return "layout";
		case KEY_PAGE_FOLDER:
			return "pages/";
		case KEY_SUFFIX:
			return ".jsp";
		case KEY_PAGE_SCRIPT_URI:
			return "/js/pages/";
		case KEY_PAGE_SCRIPT_FOLDER:
			return "static" + File.separator + "js" + File.separator +  "pages" + File.separator;
		default:
			throw new RuntimeException("Unknown settings key. ");
		}
	}
}
