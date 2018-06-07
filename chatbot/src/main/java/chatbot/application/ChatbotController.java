package chatbot.application;

import chatbot.core.classifier.Classifier;
import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * This is the main function , entry point to the chatbot. Forward everything
 * that comes here to our handler functions.
 * 
 * @author Prashanth
 *
 */
@RestController
public class ChatbotController {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;
	
	@RequestMapping(value="/chat", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseList route(@RequestBody final IncomingRequest request) throws Exception {
		try {

			Classifier obj = new Classifier(restTemplate);
			Handler newobj = obj.classify(request);
			ResponseList answer = newobj.search(request);	
			/*if ("error".equals(answer)) {
				//String responseJSON = "[{ \"comment\":\"Internal Server Error.Contact your administrator.\"}]";
				responselist.setError();
				return responselist;
				//return responseJSON;
			}*/
			return answer;
		}
		catch (Exception e) {
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;
		}
		
	}
}