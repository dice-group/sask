package org.dice_research.sask.executer_ms;


import java.util.Set;
import org.apache.log4j.Logger;
import org.dice_research.sask.executer_ms.threading.TaskExecuter;
import org.dice_research.sask.executer_ms.threading.TaskFactory;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the executer functions.
 * 
 * @author Kevin Haack
 * @author André Sonntag
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
		Set<Runnable> taskSet = TaskFactory.createTasks(restTemplate, workflow, startOperators);
		TaskExecuter executer = new TaskExecuter(taskSet);
		executer.execute();
		return "";
	}

}
