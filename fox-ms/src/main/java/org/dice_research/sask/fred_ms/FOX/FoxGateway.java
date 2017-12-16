package org.dice_research.sask.fred_ms.FOX;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Represents a gateway to the fox extractor.
 * 
 * @author Kevin Haack
 *
 */
public class FoxGateway {
	/**
	 * The service uri to the fox extractor. Online version:
	 * "http://fox-demo.aksw.org/fox"
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
			headers.set("accept", "application/x-turtle;charset=utf-8");
			headers.add("Content-Type", "application/json");

			HttpEntity<FoxDTO> entity = new HttpEntity<FoxDTO>(fox, headers);
			ResponseEntity<String> result = restTemplate.exchange(SERVICE_URI, HttpMethod.POST, entity, String.class);
			return result.getBody();
		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to fred (" + ex.getMessage() + ").", ex);
		}
	}
}
