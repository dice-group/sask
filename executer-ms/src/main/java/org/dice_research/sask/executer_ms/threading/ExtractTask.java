package org.dice_research.sask.executer_ms.threading;

import java.util.Set;
import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * This class is a thread task and responsible for data extraction.
 * 
 * @author André Sonntag
 *
 */

public class ExtractTask implements Runnable {


	private final Logger logger = Logger.getLogger(ExtractTask.class.getName());
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
		logger.info("Extractor input: "+extractorInput);
		String uri = this.getExtractorURI(this.getOperatorName());
		
		HttpHeaders headers = new HttpHeaders();
		//headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("input", extractorInput);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<String> response = restTemplate.postForEntity( uri+"/extractSimple?", request , String.class );
		
		String extractorOutput = response.getBody();
		//String extractorOutput =  this.restTemplate.getForObject(uri + "/extractSimple?input={content}", String.class, this.extractorInput);

		if (null != extractorOutput) {
			logger.info(extractorOutput);
		} else {
			logger.info("no data extracted");
		}
		
		Set<Runnable> nextOperatorList = TaskFactory.createTasks(this.restTemplate, this.wf,this.getNextOperatorList(),new String[] {extractorOutput});		
		logger.info("Next Task: "+ nextOperatorList.iterator().next().toString());

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
