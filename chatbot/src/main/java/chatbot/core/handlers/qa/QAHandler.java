/**
 * 
 */
package chatbot.core.handlers.qa;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.EntryInformation;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;
import chatbot.core.handlers.*;

/**
 * @author Prashanth
 *
 */
@Component
@PropertySource("classpath:application.yml")
public class QAHandler extends Handler {
	// Handle Hawk Service.
	private static Logger log = Logger.getLogger(QAHandler.class.getName());
	
	@Value("${qa.hawk.url}")
	private static String URL;

	
	private Response generateResponse(String incomingResponse) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(incomingResponse);
		//String text = rootNode.path("answer")
		//                      .toString();
		Response obj=new Response();
		EntryInformation entry = new EntryInformation();
		JsonNode middleNode = rootNode.path("answer").get(0);
		if(middleNode.has("URI")) { //Prashanth:Check to prevent code crash, only if JSON Node exists below code must be executed.
			entry.setUri(middleNode.get("URI").toString());
			entry.setButtonType(EntryInformation.Type.URL);
			entry.setDisplayText("Open with DBpedia"); // DBpedia links are returned right now so hardcode.
		}
		//Prashanth:Revisit using these later. Ricardo mentioned these may be wrong and we must use carousel type extractor from URL to display these below fields.
		if(middleNode.has("comment")) {
			obj.setContent(middleNode.get("comment").toString());
		}
		if(middleNode.has("thumbnail")) {
			obj.setImage(middleNode.get("thumbnail").toString());
		}
		obj.addEntry(entry);
		return obj;
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
			responselist.setMessageType(MessageType.TEXT_WITH_URL);
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = generateHTTPQuery(query);
			String response = sendHTTPRequest(sendText);
			//If in future, multiple answers need to be returned, then split it into n*1 and call below functions in a loop
			Response output = generateResponse(response); 
			responselist.addMessage(output);
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
