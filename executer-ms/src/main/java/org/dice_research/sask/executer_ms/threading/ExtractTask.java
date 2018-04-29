package org.dice_research.sask.executer_ms.threading;

import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

public class ExtractTask implements Runnable {


	private final Logger logger = Logger.getLogger(PullTask.class.getName());
	private final RestTemplate restTemplate;
	private final Operator op;
	private final Workflow wf;
	private final String extractorInput;

	public ExtractTask(RestTemplate restTemplate, Workflow wf, Operator op, String extractorInput) {
		this.restTemplate = restTemplate;
		this.wf = wf;
		this.op = op;
		this.extractorInput = extractorInput;
	}

	@Override
	public void run() {
		logger.info("Start Thread: "+ExtractTask.class.getName()+ "with Extractor: "+getOperatorName());	
		String uri = this.getExtractorURI(this.getOperatorName());
		String extractorOutput =  this.restTemplate.getForObject(uri + "/extractSimple?input={content}", String.class, this.extractorInput);
		

		if (null != extractorOutput) {
			logger.info(extractorOutput);
		} else {
			logger.info("no data extracted");
		}
		
		Set<Runnable> nextOperatorList = TaskFactory.createTasks(this.restTemplate, this.wf,this.getNextOperatorList(),new String[] {extractorOutput});
		TaskExecuter executer = new TaskExecuter(nextOperatorList);
		executer.execute();
	}

	private String getOperatorName() {
		return this.op.getContent();
	}
	
	private Set<Operator> getNextOperatorList() {
		return this.wf.getNextOperators(op);
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
