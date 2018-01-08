package org.dice_research.sask.repo_ms;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepoMsController {
	protected Logger logger = Logger.getLogger(RepoMsController.class.getName());

	@RequestMapping("/")
	public String root() {
		this.logger.info("discovery-microservice root() invoked");
		return "some root stuff here.";
	}

	
}
