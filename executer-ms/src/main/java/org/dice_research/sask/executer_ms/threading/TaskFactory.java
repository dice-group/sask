package org.dice_research.sask.executer_ms.threading;

import java.util.HashSet;
import java.util.Set;

import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

public class TaskFactory {


	public static Runnable createTask(RestTemplate restTemplate, Workflow wf, Operator op, String... operatorInput) {
		
		if(null == restTemplate || null == wf || null == op) {
			throw new IllegalArgumentException("At least one parameter is null");
		}
		
		if (op.getType().equalsIgnoreCase("file")) {
			return new PullTask(restTemplate, wf, op);

		} else if (op.getType().equalsIgnoreCase("extractor")) {
			
			if(null == operatorInput || operatorInput.length == 0) {
				throw new IllegalArgumentException("operatorInput parameter is null or emthy");
			}
			return new ExtractTask(restTemplate, wf, op,operatorInput[0]);

		} else if (op.getType().equalsIgnoreCase("database")) {
			
			if(null == operatorInput || operatorInput.length == 0) {
				throw new IllegalArgumentException("operatorInput parameter is null or emthy");
			}
			return new StoreTask(restTemplate, wf, op,operatorInput[0]);
		} else {
			throw new IllegalArgumentException("Operator is wrong or not listed");
		}
	}

	public static Set<Runnable> createTasks(RestTemplate restTemplate, Workflow wf, Set<Operator> opSet, String... operatorInput) {

		if(null == restTemplate || null == wf || null == opSet) {
			throw new IllegalArgumentException("At least one parameter is null");
		}
		
		Set<Runnable> taskSet = new HashSet<>();

		for (Operator op : opSet) {
			taskSet.add(createTask(restTemplate,wf,op, operatorInput));
		}
		return taskSet;
	}
}
