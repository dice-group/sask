package chatbot.core.handlers.rivescript;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import chatbot.core.handlers.*;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

import java.lang.String;
import java.io.IOException;
import org.json.JSONObject;
import org.apache.log4j.Logger;

public class RiveScriptOutputAnalyzer {
	private static Logger log = Logger.getLogger(RiveScriptOutputAnalyzer.class.getName());
	public Response createPlainTextResponse(String response)
	{
		Response obj = new Response();
		obj.setContent(response);
		obj.setTitle("");
		return obj;
	}
	public ResponseList HandleTextMessage(ResponseList responselist, String textMessage) throws IOException {
		if (isJSONObject(textMessage)) {
			JSONObject jsonResult = new JSONObject(textMessage);
			if (jsonResult.has("type")) {
				String typeValue = jsonResult.getString("name");
				log.info("HandleTextMessage:,type of data=" + typeValue);
				// TODO Get Output after Initializing.
				//TODO: Update Response List as TYPE_URL and update URL and other details completely based on type
			}
			// TODO:Exception Handling for this special case?
		} else {
			responselist.setMessageType(MessageType.PLAIN_TEXT);
			responselist.addMessage(createPlainTextResponse(textMessage));
			//String responseJSON = "[{ \"comment\":\"" + textMessage + "\"}]";
			//return responseJSON;
		}
		return responselist;
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