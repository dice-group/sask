package chatbot.core.handlers.extractAutomaticallyWithKeywords;

import java.io.IOException;

import org.apache.log4j.Logger;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExtractAutomaticallyWithKeywordsHandler extends Handler{
	private static Logger log = Logger.getLogger(ExtractAutomaticallyWithKeywordsHandler.class.getName());

	private final RestTemplate restTemplate;

	public ExtractAutomaticallyWithKeywordsHandler (RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
}

	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			log.info("In Search Request of Extract Automatically");

			String uri = "http://INTELLIGENTDATAASSISTANT/executeIntelligentDataAssistant";
	  		String response = this.restTemplate.getForObject(uri, String.class);	
	  		log.info("RESPONSE:"+response);
//			All the responses will be in the form of responseList object.
//			At the moment nothing is returned. Future task
			ResponseList responselist = new ResponseList();	
			return responselist;

		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("Extract Automatically, Exception in handling Automatic Extraction Queries");
			ResponseList responselist = new ResponseList();
			responselist.setError();
			log.info(e.getMessage());
			return responselist;
		}
	}
}