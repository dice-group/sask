package org.dice_research.sask.webclient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoveryController {
	
	protected Logger logger = Logger.getLogger(DiscoveryController.class.getName());
	
	@Autowired
	protected HelloRepository helloRepository;

	@RequestMapping("/")
	public String root() {
		return "index";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/datatest")
	public String accountDetails(Model model) {
		try {
			model.addAttribute("rootstuff", this.helloRepository.getRootstuff());
		} catch(Exception ex) {
			model.addAttribute("rootstuff", "exception " + ex.getMessage());
		}
		
		try {
			model.addAttribute("hello", this.helloRepository.getHello());
		} catch(Exception ex) {
			model.addAttribute("hello", "exception " + ex.getMessage());
		}
		
		try {
			model.addAttribute("hellojson", this.helloRepository.getHelloJSON());
		} catch(Exception ex) {
			model.addAttribute("hellojson", "exception " + ex.getMessage());
		}

		return "datatest";

	}
}
