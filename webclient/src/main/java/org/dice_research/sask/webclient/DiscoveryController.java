package org.dice_research.sask.webclient;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dice_research.sask.webclient.Bootstrap.Bootstrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DiscoveryController {

	/**
	 * The Logger.
	 */
	protected Logger logger = Logger.getLogger(DiscoveryController.class.getName());
	
	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping("/*")
	public String bootstrap(Model model, HttpServletRequest request) {
		Bootstrapper bootstrap = new Bootstrapper(model, request, resourceLoader);
		return bootstrap.run();
	}

}
