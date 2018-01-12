package chatbot.core.handlers.rivescript;

import org.apache.log4j.Logger;
import java.util.*;
import com.rivescript.RiveScript;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.core.handlers.*;
import java.io.IOException;
import org.json.JSONObject;

public class RiveScriptQueryHandler extends Handler {
	private RiveScript obj;
	private static Logger log = Logger.getLogger(RiveScriptQueryHandler.class.getName());

	public RiveScriptQueryHandler() {
		obj = new RiveScript();
		obj.loadDirectory("src/main/resources/rivescript");
		obj.sortReplies();
	}

	// Custom Function to check if Query is found in Rive Script.
	public boolean isQueryFound(String query) {
		String reply = obj.reply("user", query);
		if ("NOT FOUND".equals(reply)) {
			return false;
		}
		return true;
	}
	
	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			// Analyze passed Output from Rivescript for whether further
			// processing is required.
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText()
			                      .toLowerCase();
			String reply = obj.reply("user", query);
			RiveScriptOutputAnalyzer analyzer = new RiveScriptOutputAnalyzer();
			responselist = analyzer.HandleTextMessage(responselist , reply);
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
