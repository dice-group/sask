package chatbot.application;

import chatbot.core.classifier.Classifier;
import chatbot.core.handlers.Handler;
import chatbot.core.incomingrequest.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is the main function , entry point to the chatbot. Forward everything
 * that comes here to our handler functions.
 * 
 * @author Prashanth
 *
 */
@RestController
@RequestMapping("/chat")
public class Application {
	@RequestMapping(method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String route(@RequestBody final IncomingRequest request) throws Exception {
		Classifier obj = new Classifier();
		Handler newobj = obj.classify(request);
		String answer = newobj.search(request);
		return answer;
	}
}