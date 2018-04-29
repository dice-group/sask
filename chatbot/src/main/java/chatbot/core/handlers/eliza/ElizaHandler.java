package chatbot.core.handlers.eliza;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import chatbot.codeanticode.eliza.ElizaMain;
import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

@Component
public class ElizaHandler extends Handler {
	private static Logger log = Logger.getLogger(ElizaHandler.class.getName());
	private ElizaMain eliza = new ElizaMain();
	
	public ElizaHandler() {

	}
	
	@Autowired
	private  ResourceLoader resourceLoader;

	@PostConstruct
    public void postConstruct() {
        resourceLoader();
    }
	
	private void resourceLoader() {
		
		log.info("Loading the Elisa script");
		try {
			InputStream inputStream = resourceLoader.getResource("classpath:eliza/script").getInputStream();
			
			// Creating a temp script file which is loaded by Elisa
    			File file = new File("script");
			OutputStream outputStream = new FileOutputStream(file);
    			IOUtils.copy(inputStream, outputStream);
    			outputStream.close();
    			
			eliza.readScript(true, file.toString());
			
			// Delete the temp script file
			file.delete();
		} catch (IOException e) {
			log.error("resourceLoader, IO Exception while loading Elisa Script,Stack Trace=" + e.getMessage());
			e.printStackTrace();
		}
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
