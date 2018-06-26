package org.dice_research.sask.cedric_ms;

import org.dice_research.cedric_extraction.Extractor;

/**
 * This class forms a facade to Cedric extractor.
 * 
 * @author Kevin Haack
 *
 */
public class CedricMsService {

	/**
	 * The only instance.
	 */
	private static CedricMsService instance = new CedricMsService();
	/**
	 * Indicates if cedric is trained.
	 */
	private static boolean isTrained = false;

	private CedricMsService() {

	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @return
	 */
	public static CedricMsService getInstance() {
		if (!CedricMsService.isTrained) {
			Extractor.setup();
			Extractor.train();
			CedricMsService.isTrained = true;
		}

		return CedricMsService.instance;
	}

	/**
	 * Extract the passed input string with cedric.
	 * 
	 * @param input
	 *            The input to extract.
	 * @return The extracted information.
	 */
	public String extract(String input) {
		return Extractor.extract(input);
	}
}
