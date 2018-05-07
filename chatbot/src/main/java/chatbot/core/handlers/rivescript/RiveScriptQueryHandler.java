package chatbot.core.handlers.rivescript;

import java.io.IOException;

import org.apache.log4j.Logger;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;

public class RiveScriptQueryHandler extends Handler {
	private static Logger log = Logger.getLogger(RiveScriptQueryHandler.class.getName());

	//Rivescript reponse is now handled in RiveScriptOutputHandler
	
	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			// Analyze passed Output from Rivescript for whether further
			// processing is required.
			ResponseList responselist = new ResponseList();
			RiveScriptOutputAnalyzer analyzer = new RiveScriptOutputAnalyzer();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText()
			                      .toLowerCase();
			responselist = analyzer.riveHandler(query);
			return responselist;

		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("search, Exception in handling Rivescript Queries");
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;
		}

	}

}
