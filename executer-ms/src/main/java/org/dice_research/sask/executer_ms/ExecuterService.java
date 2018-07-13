package org.dice_research.sask.executer_ms;

import java.util.List;

import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the executer functions.
 * 
 * @author Kevin Haack
 *
 */
public class ExecuterService {

	/**
	 * The logger.
	 */
	private final Logger logger = Logger.getLogger(ExecuterService.class.getName());

	/**
	 * The rest template.
	 */
	private final RestTemplate restTemplate;

	public ExecuterService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Execute the passed workflow.
	 * 
	 * @param workflow
	 * @return
	 */
	public String execute(final Workflow workflow) {
		List<Operator> queue = workflow.createQueue();
		String filePath = queue.get(0)
		                       .getContent();
		String extractorServiceId = queue.get(1)
		                                 .getContent();
		String targetGraph = queue.get(2)
		                          .getContent();

		return execute(filePath, extractorServiceId, targetGraph);
	}

	/**
	 * Simple version to execute.
	 * 
	 * @param filePath
	 * @param extractorServiceId
	 * @param targetGraph
	 * @return
	 */
	public String execute(String filePath, String extractorServiceId, String targetGraph) {
		/*
		 * start simple task for execution
		 */
		Runnable task = () -> {
			logger.info("Task started...");
			logger.info("Step 1/3 (get content)");
			String content = getFileContent(filePath);
			logger.info(content);

			logger.info("Step 2/3 (extract)");
			String extractedData = extract(extractorServiceId, content);

			if (null != extractedData) {
				logger.info(extractedData);
			} else {
				logger.info("no data extracted");
			}

			if (null != extractedData) {
				logger.info("Step 3/3 (write to database)");
				writeInDb(targetGraph, extractedData);
			}

			logger.info("Task done.");
		};

		Thread thread = new Thread(task);
		thread.start();

		return "done";
	}

	private void writeInDb(String targetGraph, String data) {
		String uri = getDBURI() + "/updateGraph";

		try {
			restTemplate.postForObject(uri, data, String.class);
		} catch (Exception ex) {
			logger.info("failed to write to database (" + ex.getMessage() + ")");
		}
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
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		case "OPEN-IE-MS":
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		case "SPOTLIGHT-MS":
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		default:
			throw new RuntimeException("Unsupported extractor '" + extractor + "'");
		}

		return uri;
	}
}
