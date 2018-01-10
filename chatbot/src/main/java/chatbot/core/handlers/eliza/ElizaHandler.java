package chatbot.core.handlers.eliza;

import com.fasterxml.jackson.core.JsonProcessingException;
import chatbot.codeanticode.eliza.ElizaMain;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;
import chatbot.core.handlers.Handler;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ElizaHandler extends Handler {
	private static Logger log = Logger.getLogger(ElizaHandler.class.getName());

	public ElizaHandler() {

	}
	public Response createNewResponse(String response)
	{
		Response obj = new Response();
		obj.setContent(response);
		obj.setTitle("");
		return obj;
	}
	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			ElizaMain eliza = new ElizaMain();
			eliza.readScript(true, "src/main/resources/eliza/script");
			String response = eliza.processInput(query);
			log.info("search:Got Response , response= " + response);
			responselist.setMessageType(MessageType.PLAIN_TEXT);
			responselist.addMessage(createNewResponse(response)); // Add Response to Final String
			//String responseJSON = "[{ \"comment\":\"" + response + "\"}]";
			return responselist;
		}
		/*catch (JsonProcessingException e) {
			// Check if we can create a logger.
			log.error("search, JsonProcessingException in handling QA Queries,Stack Trace=" + e.getMessage());
			return "error";
		} catch (IOException e) {
			// Check if we can create a logger.
			log.error("search, IOException in handling QA Queries,Stack Trace=" + e.getMessage());
			return "error";
		} */
		catch (Exception e) {
			// Check if we can create a logger.
			log.error("search, Exception in handling QA Queries,Stack Trace=" + e.getMessage());
			ResponseList responselist = new ResponseList();
			responselist.setMessageType(MessageType.PLAIN_TEXT);
			responselist.setError();
			return responselist;
		}
	}

}
