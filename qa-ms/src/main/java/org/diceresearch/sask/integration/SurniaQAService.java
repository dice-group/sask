package org.diceresearch.sask.integration;

import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;


@Service
public class SurniaQAService {

    private static final Logger logger = Logger.getLogger(SurniaQAService.class);

    private static final String SURNIA_URL = "http://localhost:8181/ask-gerbil"; // TODO: 02/07/2018 should be read from properties file

    private final RestTemplate restTemplate;


    public SurniaQAService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Calls SurniaQA with query and returns the response
     *
     * @param query
     * @return
     */

    // TODO: 02/07/2018 temporarily returning String response, return proper response DTO if needed
    public String askSurnia(String query) {
        logger.info("Requesting SurniaQA for following query: " + query);
        URI uri = new UriTemplate(SURNIA_URL).expand(query);
//        return exchange(uri, String.class);
        return "Hello from QA Service";
    }

    /**
     * Generic method to return response from a RestCall endpoint
     *
     * @param uri
     * @param response
     * @param <T>
     * @return
     */
    private <T> T exchange(URI uri, Class<T> response) {
        RequestEntity<?> requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<T> responseEntity = this.restTemplate.exchange(requestEntity, response);
        return responseEntity.getBody();
    }
}
