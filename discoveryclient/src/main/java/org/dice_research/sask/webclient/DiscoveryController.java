package org.dice_research.sask.webclient;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscoveryController {
	protected Logger logger = Logger.getLogger(DiscoveryController.class.getName());

	@RequestMapping("/")
	public String root() {
		this.logger.info("discovery-microservice root() invoked");
		return "some root stuff here.";
	}

	@RequestMapping("/hello")
	public String helloString() {
		this.logger.info("discovery-microservice hello() invoked");
		return "Hello projectgroup!";
	}

	@RequestMapping("/hellojson")
	public Hello helloJSON() {
		this.logger.info("discovery-microservice hellojson() invoked");
		Hello h = new Hello();
		h.setMessage("Hello Projectgroup (as json)!");
		return h;
	}

	@RequestMapping("/info")
	public String info() {
		this.logger.info("discovery-microservice info() invoked");
		StringBuilder builder = new StringBuilder();

		builder.append("<!DOCTYPE html>");
		builder.append("<html><head>");
		builder.append("<meta charset=\"utf-8\">");
		builder.append("<title>Microservice running</title>");
		builder.append("</head><body>");
		builder.append("<h1>Jep, this REST controller is running.</h1>");
		builder.append("</body></html>");

		return builder.toString();
	}
}
