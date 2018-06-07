package chatbot.core.handlers.extractAutomaticallyWithKeywords;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class RestCall {
	private static Logger log = Logger.getLogger(RestCall.class.getName());

//	@Autowired
////	@LoadBalanced
//	    private RestTemplate restTemplate;
//
//	  	public String makeRestCall() {
//		  	 String uri = "http://REPO-MS/getHdfsStructure?location=repo";  
//
//	  		log.info("Before rest call");
////	  	    RestTemplate restTemplate = new RestTemplate();
//
//	  		String response = restTemplate.getForObject(uri, String.class);	
//	  		log.info(response);
//			log.info("After rest call");
//	  		return response;
//					
//	    }
}
