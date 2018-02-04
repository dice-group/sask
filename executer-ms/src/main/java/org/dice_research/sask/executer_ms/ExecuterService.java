package org.dice_research.sask.executer_ms;

import org.springframework.web.client.RestTemplate;

public class ExecuterService {

	private RestTemplate restTemplate;
	
	public ExecuterService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String execute(String data, String extractor, String targetgraph) {
		String content = getFileContent(data);
		String uri = getExtractorURI(extractor);
		
		String result = this.restTemplate.getForObject(uri
                + "/extractSimple?input={content}", String.class, content);
		
		// write in db
		
		return result;
	}

	private String getFileContent(String file) {
		return "Barack Obama is married to Michelle Obama.";
	}

	private String getExtractorURI(String extractor) {
		String uri;
		
		switch (extractor) {
		case "fred":
			uri = "http://FRED-MS";
			break;
		case "fox":
			uri = "http://FOX-MS";
			break;
		case "spotlight":
			uri = "http://SPOTLIGHT-MS";
			break;
		default:
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		}

		return uri;
	}
}
