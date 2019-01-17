package org.diceresearch.sask.integration;

import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


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
    public ResponseEntity<String> askSurnia(String query) {
        logger.info("Requesting SurniaQA for following query: " + query);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", query);
        params.add("lang", "en");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return this.restTemplate.postForEntity(SURNIA_URL, request, String.class);
    }
}
