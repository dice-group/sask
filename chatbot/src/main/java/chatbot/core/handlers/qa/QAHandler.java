/**
 * 
 */
package chatbot.core.handlers.qa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import chatbot.core.incomingrequest.IncomingRequest;
import chatbot.core.handlers.*;

/**
 * @author Prashanth
 *
 */
public class QAHandler extends Handler {
	// Handle Hawk Service.
	private static final String URL = "http://localhost:8181/simple-search?query="; // URL
	                                                                                // of
	                                                                                // Hawk
	                                                                                // Service.

	public QAHandler() {

	}

	private String generateResponse(String incomingResponse) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(incomingResponse);
		String text = rootNode.path("answer")
		                      .toString();
		return text;
	}

	private String generateHTTPQuery(String question) {
		String[] strgs = question.split(" "); // Remove and create words instead
		                                      // of passing complete sentences.
		                                      // Follow format specified in
		                                      // gitthub rdocumentation
		String query = "";
		for (int j = 0; j < strgs.length; j++) {
			query += strgs[j] + "+";
		}
		query = query.substring(0, query.length() - 1);
		String URLText = URL + query;
		return URLText;
	}

	public String search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = generateHTTPQuery(query);
			String response = sendHTTPRequest(sendText);
			String output = generateResponse(response);
			return output;
		} catch (Exception e) {
			// Check if we can create a logger.
			System.out.println("Exception occured in qa.java");
			throw new InternalError();
		}
	}
}
