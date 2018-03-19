package org.dice_research.sask.executer_ms;

import java.util.List;

import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

public class ExecuterService {

	/**
	 * The rest template.
	 */
	private RestTemplate restTemplate;

	public ExecuterService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Execute the passed workflow.
	 * 
	 * @param workflow
	 * @return
	 */
	public String execute(Workflow workflow) {
		/*
		 * simple version only allow a queue length of 3.
		 */
		List<Operator> queue = workflow.createQueue();
		String filePath = queue.get(0)
		                       .getContent();
		String extractorServiceId = queue.get(1)
		                                 .getContent();
		String targetGraph = queue.get(2)
		                          .getContent();
		return execute(filePath, extractorServiceId, targetGraph);
	}

	public String execute(String data, String extractor, String targetgraph) {
		String content = getFileContent(data);
		String extractedData = extract(extractor, content);
		writeInDb(extractedData);
		// write in db

		return extractedData;
	}
	
	private void writeInDb(String data) {
		String uri = getDBURI();
		this.restTemplate.getForObject(uri + "/updateGraph?input={data}", String.class, data);
	}
	
	private String extract(String extractor, String content) {
		String uri = getExtractorURI(extractor);
		return this.restTemplate.getForObject(uri + "/extractSimple?input={content}", String.class, content);
	}

	private String getFileContent(String file) {
		String uri = getRepoURI();
		return this.restTemplate.getForObject(uri + "/readFile?location=repo&path={file}", String.class, file);
	}
	
	private String getDBURI() {
		return "http://DATABASE-MS";
	}
	
	private String getRepoURI() {
		return "http://REPO-MS";
	}
	
	private String getExtractorURI(String extractor) {
		String uri;

		switch (extractor) {
		case "CEDRIC-MS":
			uri = "http://CEDRIC-MS";
			break;
		case "FRED-MS":
			uri = "http://FRED-MS";
			break;
		case "FOX-MS":
			uri = "http://FOX-MS";
			break;
		case "SPOTLIGHT-MS":
			uri = "http://SPOTLIGHT-MS";
			break;
		default:
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		}

		return uri;
	}
}
