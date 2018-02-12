package org.dice_research.sask.cedric_ms;

import org.dice_research.cedric.Extractor;

public class CedricMsService {

	private static CedricMsService instance = new CedricMsService();
	private static boolean isTrained = false;

	private CedricMsService() {

	}

	public static CedricMsService getInstance() {
		if(!CedricMsService.isTrained) {
			Extractor.setup();
			Extractor.train();
			CedricMsService.isTrained = true;
		}
		
		return CedricMsService.instance;
	}
	
	public String extract(String input) {
		return Extractor.extract(input);
	}
}
