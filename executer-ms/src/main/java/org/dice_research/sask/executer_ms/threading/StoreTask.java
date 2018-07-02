package org.dice_research.sask.executer_ms.threading;

import java.util.Set;
import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class StoreTask implements Runnable {

	private final Logger logger = Logger.getLogger(StoreTask.class.getName());
	private final RestTemplate restTemplate;
	private final Operator op;
	private final Workflow wf;
	private final String data;
	// private final String targetGraph;

	public StoreTask(RestTemplate restTemplate, Workflow wf, Operator op, String data) {
		this.restTemplate = restTemplate;
		this.wf = wf;
		this.op = op;
		this.data = data;
	}

	@Override
	public void run() {
			logger.info("Start Thread: " + StoreTask.class.getName());	
			String uri = "http://DATABASE-MS/updateGraph";
			restTemplate.postForObject(uri, data, String.class);
	}

	private String getTargetGrapName() {
		return this.op.getContent();
	}

	private Set<Operator> getNextOperatorList() {
		return this.wf.getNextOperators(op);
	}

}
