/**
 * 
 */
package chatbot.core.handlers.qa;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.EntryInformation;
import chatbot.io.response.EntryInformation.Type;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

/**
 * @author Prashanth
 *
 */
@Component
@PropertySource("classpath:application.yml")
public class QAHandler extends Handler {
	// Handle Hawk Service.
	private static Logger log = Logger.getLogger(QAHandler.class.getName());
	
	private static String URL = "http://185.2.103.92:8081/tebaqa/qa-simple?query=";

	
	private Response generateResponse(String incomingResponse) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(incomingResponse);
		Response obj=new Response();
		
		//JsonNode middleNode = rootNode.path("answers").get(0);
		JsonNode answers = rootNode.get("answers");
		if(answers.isArray()) {
			 for (final JsonNode objNode : answers) {
				 EntryInformation entry = new EntryInformation();
				 entry.setUri(objNode.asText());
				 String fileName = FilenameUtils.getName(objNode.asText());
				 fileName = fileName.replaceAll("_", " ");
				 entry.setDisplayText(fileName);
				 entry.setButtonType(Type.URL);
				 obj.addEntry(entry);
				 
			 }
		}
		
		/*if(middleNode.has("URI")) { //Prashanth:Check to prevent code crash, only if JSON Node exists below code must be executed.
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
		}*/
		//obj.addEntry(entry);
		return obj;
	}

	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			ResponseList responselist = new ResponseList();
			responselist.setMessageType(MessageType.TEXT_WITH_URL);
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = URL + URLEncoder.encode(query, "UTF-8");
			String response = sendHTTPRequest(sendText);
			//If in future, multiple answers need to be returned, then split it into n*1 and call below functions in a loop
			Response output = generateResponse(response); 
			if(log.isDebugEnabled())
				output.setClassPredicted("Class Predicted - QA");
			responselist.addMessage(output);
			return responselist;
		} catch (JsonProcessingException e) {
			// Check if we can create a logger.
			log.error("search, JsonProcessingException in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			if(log.isDebugEnabled()) {
				Response response = new Response();
				response.setClassPredicted("Class Predicted - QA");
				responselist.addMessage(response);
			}
			responselist.setError();
			return responselist;
		} catch (IOException e) {
			// Check if we can create a logger.
			log.error("search, IOException in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			if(log.isDebugEnabled()) {
				Response response = new Response();
				response.setClassPredicted("Class Predicted - QA");
				responselist.addMessage(response);
			}
			responselist.setError();
			return responselist;
		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("search, Exception in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			if(log.isDebugEnabled()) {
				Response response = new Response();
				response.setClassPredicted("Class Predicted - QA");
				responselist.addMessage(response);
			}
			responselist.setError();
			return responselist;

		}
	}
}
