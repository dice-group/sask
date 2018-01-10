/**
 * 
 */
package chatbot.core.handlers.qa;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;
import chatbot.core.handlers.*;

/**
 * @author Prashanth
 *
 */
public class QAHandler extends Handler {
	// Handle Hawk Service.
	private static Logger log = Logger.getLogger(QAHandler.class.getName());
	private static final String URL = "http://localhost:8181/simple-search?query="; // URL

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

	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = generateHTTPQuery(query);
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
