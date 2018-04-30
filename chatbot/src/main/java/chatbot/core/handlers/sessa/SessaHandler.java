/**
 * 
 */
package chatbot.core.handlers.sessa;
//import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.EntryInformation;
import chatbot.io.response.EntryInformation.Type;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;
import chatbot.core.handlers.*;
import java.net.URLEncoder;
/**
 * @author Prashanth
 *
 */
@Component
@PropertySource("classpath:application.yml")
public class SessaHandler extends Handler {
	// Handle Hawk Service.
	private static Logger log = Logger.getLogger(SessaHandler.class.getName());
	
	@Value("${sessa.search.url}")
	private static String URL;


	private Response generateResponse(String incomingResponse) throws JsonProcessingException, IOException {
		Response obj=new Response();
		obj.setContent("");
		obj.setTitle("");
		incomingResponse = incomingResponse.substring(1,incomingResponse.length()-1);
		log.info(incomingResponse);
		String[] parts = incomingResponse.split(",");
		//String response = "[";
		for (int i = 0; i < parts.length; ++i)
		{
			EntryInformation entry = new EntryInformation();
			entry.setUri(parts[i]);
			entry.setDisplayText("Open with DBpedia"); // DBpedia links are returned right now so hardcode.
			entry.setButtonType(Type.URL);
			obj.addEntry(entry);
			//response += "{\"URI\":" + parts[i] + "},";
		}
		//response = response.substring(0, response.length() - 1);
        //        response += "]";
		return obj;
	}
	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			ResponseList responselist = new ResponseList();
			responselist.setMessageType(MessageType.URL);
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			String sendText = URL + URLEncoder.encode(query, "UTF-8");
			String response = sendHTTPRequest(sendText);
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
