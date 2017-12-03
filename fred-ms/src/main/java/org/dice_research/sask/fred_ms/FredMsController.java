package org.dice_research.sask.fred_ms;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FredMsController {
	protected Logger logger = Logger.getLogger(FredMsController.class.getName());
	
	
	@RequestMapping(value = "/info", produces = "application/rdf+json", method = RequestMethod.GET)
	public String fred() {
			
		String text = "Miles Davis was an american jazz musician.";
		String wfd = "b";
		String annotation = "earmark";
		
		String uri = "http://wit.istc.cnr.it/stlab-tools/fred?text="+text+"&wfd_profile="+wfd+"&textannotation="+annotation;
		RestTemplate rest = new RestTemplate();
		String ret = rest.getForObject(uri, String.class);
		
		return ret;
	}
	
	
}
