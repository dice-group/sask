package org.dice_research.sask.executer_ms;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * The class provides the REST interface for the microservice.
 * 
 * @author Kevin Haack
 */
@RestController
public class ExecuterMsController {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	private Logger logger = Logger.getLogger(ExecuterMsController.class.getName());

	@RequestMapping("/executeWorkflow")
	public String executeWorkflow(@RequestBody Workflow workflow) {
		logger.info("executer-microservice executeWorkflow(@RequestBody Workflow workflow)");
		ExecuterService service = new ExecuterService(restTemplate);
		return service.execute(workflow);
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		this.logger.error("EXECUTER-microservice IllegalArgumentException: " + e.getMessage());
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
		this.logger.error(e);
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
