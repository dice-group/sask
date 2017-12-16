package chatbot.core.handlers.rivescript;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import chatbot.core.handlers.*;
import java.lang.String;
import java.io.IOException;
import org.json.JSONObject;
import org.apache.log4j.Logger;

public class RiveScriptOutputAnalyzer {
	private static Logger log = Logger.getLogger(RiveScriptOutputAnalyzer.class.getName());

	public String HandleTextMessage(String textMessage) throws IOException {
		if (isJSONObject(textMessage)) {
			JSONObject jsonResult = new JSONObject(textMessage);
			if (jsonResult.has("type")) {
				String typeValue = jsonResult.getString("name");
				log.info("HandleTextMessage:,type of data=" + typeValue);
				// TODO Get Output after Initializing.
			}
			// TODO:Exception Handling for this special case?
		} else {
			String responseJSON = "[{ \"comment\":\"" + textMessage + "\"}]";
			return responseJSON;
		}
		return textMessage;
	}

	public boolean isJSONObject(String string) {
		try {
			new ObjectMapper().readTree(string);
			return true;
		} catch (Exception e) {
			log.error("isJSON Object, Not a JSON String");
			return false;
		}
	}
}