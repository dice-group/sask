package chatbot.core.handlers.eliza;

import com.fasterxml.jackson.core.JsonProcessingException;
import chatbot.codeanticode.eliza.ElizaMain;
import chatbot.core.incomingrequest.IncomingRequest;
import chatbot.core.handlers.Handler;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ElizaHandler extends Handler {
	private static Logger log = Logger.getLogger(ElizaHandler.class.getName());

	public ElizaHandler() {

	}

	public String search(IncomingRequest request) throws JsonProcessingException, IOException {
		try {
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			ElizaMain eliza = new ElizaMain();
			eliza.readScript(true, "src/main/resources/eliza/script");
			String response = eliza.processInput(query);
			log.info("search:Got Response , response= " + response);
			String responseJSON = "[{ \"comment\":\"" + response + "\"}]";
			return responseJSON;
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
			return "error";
		}
	}

}
