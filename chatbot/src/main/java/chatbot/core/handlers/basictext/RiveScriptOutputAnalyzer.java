package chatbot.core.handlers.basictext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import chatbot.core.handlers.*;
import java.lang.String;
import java.io.IOException;
import org.json.JSONObject;

public class RiveScriptOutputAnalyzer {

	public String HandleTextMessage(String textMessage) throws IOException {
		if (isJSONObject(textMessage)) {
			JSONObject jsonResult = new JSONObject(textMessage);
			if (jsonResult.has("type")) {
				String typeValue = jsonResult.getString("name");
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
			System.out.println("Exception Occured IsJSONObject");
			return false;
		}
	}
}