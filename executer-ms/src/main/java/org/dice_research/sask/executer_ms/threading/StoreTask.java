package org.dice_research.sask.executer_ms.threading;

import java.util.Set;

import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

public class StoreTask implements Runnable{

	private final Logger logger = Logger.getLogger(StoreTask.class.getName());
	private final RestTemplate restTemplate;
	private final Operator op;
	private final Workflow wf;
	private final String targetGraph;
	private final String data;
	
	
	public StoreTask(RestTemplate restTemplate, Workflow wf, Operator op, String data) {
		this.restTemplate = restTemplate;
		this.wf = wf;
		this.op = op;
		this.data = data;
	}
	
	@Override
	public void run() {

		String uri = "http://DATABASE-MS/updateGraph";

		try {
			restTemplate.postForObject(uri, data, String.class);
		} catch (Exception ex) {
			logger.info("failed to write to database (" + ex.getMessage() + ")");
		}

		
		
	}
	
	private String getTargetGrapName() {
		return this.op.getContent();
	}
	
	private Set<Operator> getNextOperatorList() {
		return this.wf.getNextOperators(op);
	}

}
