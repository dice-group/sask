package org.dice_research.sask.fred_ms;

import org.dice_research.sask.fred_ms.DTO.FredDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
/**
 * @author André Sonntag
 */
public class FredService {
	private RestTemplate restTemplate;
	
	public FredService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	/**
	 * Extract the passed fredDTO.
	 * 
	 * @param fred
	 *            The fredDTO
	 * @return the extraction result.
	 */
	public String extract(FredDTO fred) {
		String input = fred.getInput();
		String wfd = fred.getWfdprofile();
		String annotation = fred.getTextannotation();
		String format = fred.getFormat();

		// URI creating
		String uri;
		try {
			String host = "http://wit.istc.cnr.it/stlab-tools/fred";
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(host)
			                                                   .queryParam("text", input)
			                                                   .queryParam("wfd_profile", wfd)
			                                                   .queryParam("textannotation", annotation)
			                                                   .queryParam("format", format);
			
			uri = builder.build()
			             .toUriString();
		} catch (Exception ex) {
			throw new RuntimeException("Unable to build fred uri (" + ex.getMessage() + ").", ex);
		}

		// Build header for FRED request
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("accept", format);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			return result.getBody();
		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to fred (" + ex.getMessage() + ").", ex);
		}
	}
}
