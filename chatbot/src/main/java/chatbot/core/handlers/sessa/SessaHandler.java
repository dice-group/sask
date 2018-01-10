/**
 * 
 */
package chatbot.core.handlers.sessa;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;
import chatbot.core.handlers.*;
import java.io.*;
import java.util.*;
import java.lang.Object.*;
import java.net.URLEncoder;
/**
 * @author Prashanth
 *
 */
public class SessaHandler extends Handler {
	// Handle Hawk Service.
	private static Logger log = Logger.getLogger(SessaHandler.class.getName());
	private static final String URL = "http://localhost:7070/search?query="; // URL

	public SessaHandler() {

	}

	private String generateResponse(String incomingResponse) throws JsonProcessingException, IOException {
		incomingResponse = incomingResponse.substring(1,incomingResponse.length()-1);
		log.info(incomingResponse);
		String[] parts = incomingResponse.split(",");
		String response = "[";
		for (int i = 0; i < parts.length; ++i)
		{
			response += "{\"URI\":" + parts[i] + "},";
		}
		response = response.substring(0, response.length() - 1);
                response += "]";
		return response;
	}
	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = URL + URLEncoder.encode(query, "UTF-8");
			String response = sendHTTPRequest(sendText);
			String output = generateResponse(response);
			return responselist;
		} catch (JsonProcessingException e) {
			// Check if we can create a logger.
			log.error("search, JsonProcessingException in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;
		} catch (IOException e) {
			// Check if we can create a logger.
			log.error("search, IOException in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;
		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("search, Exception in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;

		}
	}
}
