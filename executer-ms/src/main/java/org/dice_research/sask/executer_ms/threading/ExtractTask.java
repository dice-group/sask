package org.dice_research.sask.executer_ms.threading;

import java.util.concurrent.Callable;

import org.springframework.web.client.RestTemplate;

public class ExtractTask implements Callable<String[]> {

	private RestTemplate restTemplate;
	private String content;
	private String extractorURI;
	private String extractorServiceId;
	
	public ExtractTask(RestTemplate restTemplate, String content, String extractorServiceId,String extractorURI) {
		super();
		this.restTemplate = restTemplate;
		this.content = content;
		this.extractorURI = extractorURI;
		this.extractorServiceId = extractorServiceId;
	}

	@Override
	public String[] call() throws Exception {
		return new String[]{extractorServiceId,this.restTemplate.getForObject(extractorURI + "/extractSimple?input={content}", String.class, content)};
	}

}
