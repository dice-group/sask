package org.dice_research.sask.executer_ms.threading;

import java.util.HashSet;
import java.util.Set;
import org.dice_research.sask.executer_ms.workflow.Operator;
import org.dice_research.sask.executer_ms.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible to create the right tasks/threads.
 * 
 * @author André Sonntag
 *
 */
public class TaskFactory {

	private static Runnable createTask(RestTemplate restTemplate, Workflow wf, Operator op, String... operatorInput) {

		if (op.getType().equalsIgnoreCase("file")) {
			return new PullTask(restTemplate, wf, op);

		} else if (op.getType().equalsIgnoreCase("extractor")) {

			if (null == operatorInput || operatorInput.length == 0) {
				throw new IllegalArgumentException("operatorInput parameter is null or emthy");
			}
			return new ExtractTask(restTemplate, wf, op, operatorInput[0]);

		} else if (op.getType().equalsIgnoreCase("database")) {

			if (null == operatorInput || operatorInput.length == 0) {
				throw new IllegalArgumentException("operatorInput parameter is null or emthy");
			}
			return new StoreTask(restTemplate, wf, op, operatorInput[0]);
		} else {
			throw new IllegalArgumentException("Operator is wrong or not listed");
		}
	}

	public static Set<Runnable> createTasks(RestTemplate restTemplate, Workflow wf, Set<Operator> opSet,
			String... operatorInput) {

		if (null == restTemplate || null == wf || null == opSet) {
			throw new IllegalArgumentException("At least one parameter is null");
		}

		Set<Runnable> taskSet = new HashSet<>();

		for (Operator op : opSet) {
			taskSet.add(createTask(restTemplate, wf, op, operatorInput));
		}
		return taskSet;
	}

	public static Set<Runnable> createTasks(RestTemplate restTemplate, Workflow wf, Set<Operator> opSet) {
		return createTasks(restTemplate, wf, opSet, null);
	}
}
