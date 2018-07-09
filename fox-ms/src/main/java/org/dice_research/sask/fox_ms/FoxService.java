package org.dice_research.sask.fox_ms;

import org.dice_research.sask.fox_ms.DTO.FoxDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * Represents a gateway to the fox extractor.
 * 
 * @author Kevin Haack
 *
 */
public class FoxService {
	/**
	 * The service uri to the fox extractor. Online version:
	 * http://fox-demo.aksw.org/fox University version:
	 * http://fox.cs.uni-paderborn.de:4444/fox
	 */
	private static final String SERVICE_URI = "http://fox.cs.uni-paderborn.de:4444/fox";
	/**
	 * The spring restTemplate.
	 */
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Extract the passed foxDTO.
	 * 
	 * @param fox
	 *            The foxDTO
	 * @return the extraction result.
	 */
	public String extract(FoxDTO fox) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/json, text/plain, */*");
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept-Language", "de,en-US;q=0.7,en;q=0.3");

			HttpEntity<FoxDTO> entity = new HttpEntity<FoxDTO>(fox, headers);
			String result = restTemplate.postForObject(SERVICE_URI, entity, String.class);
			return result;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to fred (" + ex.getMessage() + ").", ex);
		}
	}
}
