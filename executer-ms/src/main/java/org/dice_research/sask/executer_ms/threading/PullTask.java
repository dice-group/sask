package org.dice_research.sask.executer_ms.threading;

import java.util.Set;
import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.web.client.RestTemplate;

/**
 * This class is a thread task and responsible to pull the data from the
 * repository.
 * 
 * @author André Sonntag
 */
public class PullTask implements Runnable {

	private final Logger logger = Logger.getLogger(PullTask.class.getName());
	private final RestTemplate restTemplate;
	private final Operator op;
	private final Workflow wf;
	private String content;

	public PullTask(RestTemplate restTemplate, Workflow wf, Operator op) {
		this.restTemplate = restTemplate;
		this.wf = wf;
		this.op = op;
	}

	@Override
	public void run() {

		logger.info("Start Thread: " + PullTask.class.getName() + " File: " + this.getFilePath());

		String filePath = this.getFilePath();
		content = this.restTemplate.getForObject("http://REPO-MS/readFile?location=repo&path={file}", String.class,
				filePath);

		if (this.getNextOperatorList().size() != 0) {
			Set<Runnable> nextOperatorList = TaskFactory.createTasks(this.restTemplate, this.wf,
					this.getNextOperatorList(), new String[] { content });

			TaskExecuter executer = new TaskExecuter(nextOperatorList);
			executer.execute();
		}

	}

	private Set<Operator> getNextOperatorList() {
		return this.wf.getNextOperators(op);
	}

	private String getFilePath() {
		return this.op.getContent();
	}

	public String getResponse() {
		return this.content;
	}
}
