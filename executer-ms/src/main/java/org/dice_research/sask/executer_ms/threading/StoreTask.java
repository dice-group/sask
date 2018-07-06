package org.dice_research.sask.executer_ms.threading;

import java.util.Set;
import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
		String uri = "http://DATABASE-MS";
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("input", data);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(uri + "/updateGraph?", request, String.class);
		logger.info(response.getStatusCode());
	}

	private String getTargetGrapName() {
		return this.op.getContent();
	}

	private Set<Operator> getNextOperatorList() {
		return this.wf.getNextOperators(op);
	}

}
