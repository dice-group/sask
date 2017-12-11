package org.dice_research.sask.dbpedia_ms;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBPediaMSController {
	protected Logger logger = Logger.getLogger(DBPediaMSController.class.getName());

	@RequestMapping("/info")
	public String extract() {
		this.logger.info("discovery-microservice root() invoked");
		return "some root stuff here.";
	}

}
