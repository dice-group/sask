package org.dice_research.sask.taipan_ms;

import java.util.logging.Logger;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the executer functions.
 * 
 * @author André Sonntag
 *
 */
public class TaipanMsService {

	/**
	 * The logger.
	 */
	private final Logger logger = Logger.getLogger(TaipanMsService.class.getName());

	/**
	 * The rest template.
	 */
	private final RestTemplate restTemplate;

	public TaipanMsService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	
}
