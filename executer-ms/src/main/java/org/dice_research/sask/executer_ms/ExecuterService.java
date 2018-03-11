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
		String uri = getExtractorURI(extractor);

		String result = this.restTemplate.getForObject(uri + "/extractSimple?input={content}", String.class, content);

		// write in db

		return result;
	}

	private String getFileContent(String file) {
		return "Barack Obama is married to Michelle Obama.";
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
