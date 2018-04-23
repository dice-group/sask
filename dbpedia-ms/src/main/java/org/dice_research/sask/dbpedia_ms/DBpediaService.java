package org.dice_research.sask.dbpedia_ms;

import java.util.Locale;

import org.dice_research.sask.dbpedia_ms.DTO.DBpediaDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The gateway to the dbpedia extractor.
 * 
 * @author Suganya Kannan, Kevin Haack
 *
 */
public class DBpediaService {

	/**
	 * The service url.
	 */
	private static final String SERVICE_URL = "http://model.dbpedia-spotlight.org/en/annotate";
	/**
	 * The rest template.
	 */
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * Extract the passed dto with the extractor.
	 * 
	 * @param dbpedia
	 *            The data transfer object
	 * @return The extraction result.
	 */
	public String extract(DBpediaDTO dbpedia) {
		// URI creating
		String uri = DBpediaService.createURI(dbpedia);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "application/json");
			HttpEntity<String> entity = new HttpEntity<String>("", headers);
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			return result.getBody();
		} catch (Exception ex) {
			throw new RuntimeException("Failed to send input to dbpedia (" + ex.getMessage() + ").", ex);
		}
	}

	/**
	 * Create the uri from the passed dbpediaDTO.
	 * 
	 * @param dbpedia
	 *            The data transfer object
	 * @return The uri.
	 */
	private static String createURI(DBpediaDTO dbpedia) {
		try {
			String text = dbpedia.getText();
			String confidence = String.format(Locale.ENGLISH, "%.2f", dbpedia.getConfidence());
			String support = "" + dbpedia.getSupport();
			String spotter = dbpedia.getSpotter();
			String disambiguator = dbpedia.getDisambiguator();
			String policy = dbpedia.getPolicy();
			String types = dbpedia.getTypes();
			String sparql = dbpedia.getSparql();

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(SERVICE_URL)
			                                                   .queryParam("text", text)
			                                                   .queryParam("confidence", confidence)
			                                                   .queryParam("support", support)
			                                                   .queryParam("spotter", spotter)
			                                                   .queryParam("disambiguator", disambiguator)
			                                                   .queryParam("policy", policy)
			                                                   .queryParam("types", types)
			                                                   .queryParam("sparql", sparql);

			return builder.build()
			              .toUriString();
		} catch (Exception ex) {
			throw new RuntimeException("Unable to build DBpedia uri (" + ex.getMessage() + ").", ex);
		}
	}
}
