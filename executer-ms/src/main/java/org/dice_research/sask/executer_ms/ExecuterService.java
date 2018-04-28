package org.dice_research.sask.executer_ms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.threading.ExtractTask;
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

		Set<Operator> startOperators = workflow.getStartOperatorSet();
		Set<Operator> nextOpSet = null;
		
		for (Operator op : startOperators) {
			nextOpSet = workflow.getNextOperator(op);
			execute(op.getContent(), nextOpSet);
		}

		return "";
		/*
		 * String filePath = queue.get(0) .getContent(); String extractorServiceId =
		 * queue.get(1) .getContent(); String targetGraph = queue.get(2) .getContent();
		 */
		// return execute(filePath, extractorServiceId, targetGraph);
	}

	public HashMap<String, String> execute(String filePath, Set<Operator> extractorServiceIds) {

		ExecutorService executorService = Executors.newFixedThreadPool(extractorServiceIds.size());
		String fileContent = getFileContent(filePath);
		HashMap<String, String> resultMap = new HashMap<>();
		List<Callable<String[]>> tasksMap = new ArrayList<>();

		for (Operator extractorServiceId : extractorServiceIds) {
			tasksMap.add(new ExtractTask(this.restTemplate, fileContent, extractorServiceId.getContent(),
					getExtractorURI(extractorServiceId.getContent())));
		}

		List<Future<String[]>> futureTaskList;
		String[] extractedData;

		try {
			futureTaskList = executorService.invokeAll(tasksMap);
			for (Future<String[]> f : futureTaskList) {
				extractedData = f.get();
				resultMap.put(extractedData[0], extractedData[1]);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		executorService.shutdown();

		return resultMap;
	}

	// not needed anymore
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

	// not needed anymore
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
