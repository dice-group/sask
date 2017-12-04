package chatbot.core.handlers.basictext;

import java.util.*;
import com.rivescript.RiveScript;
import chatbot.core.incomingrequest.IncomingRequest;
import chatbot.core.handlers.*;
import java.io.IOException;
import org.json.JSONObject;
public class BasicTextHandler extends Handler {
	private RiveScript obj;

	public BasicTextHandler() {
		obj = new RiveScript();
		obj.loadDirectory("src/main/resources/rivescript");
		obj.sortReplies();
	}
	// Custom Function to check if Query is found in Rive Script.
	public boolean isQueryFound(String query) {
		String reply = obj.reply("user", query);
		System.out.println("->" + reply + ";");
		if ("NOT FOUND".equals(reply)) {
			return false;
		}
		return true;
	}

	public String search(IncomingRequest request) throws IOException {
		try {
			// Analyze passed Output from Rivescript for whether further
			// processing is required.
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText()
			                      .toLowerCase();
			String reply = obj.reply("user", query);
			RiveScriptOutputAnalyzer analyzer = new RiveScriptOutputAnalyzer();
			String output = analyzer.HandleTextMessage(reply);
			return output;

		} catch (Exception e) {
			// Check if we can create a logger.
			System.out.println("Exception occured in qa.java");
			throw new InternalError();
		}

	}

}
