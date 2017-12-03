package chatbot.core.handlers.eliza;

import java.io.IOException;



import com.fasterxml.jackson.core.JsonProcessingException;

import chatbot.codeanticode.eliza.ElizaMain;
import chatbot.core.incomingrequest.IncomingRequest;
import chatbot.core.handlers.Handler;

public class ElizaHandler extends Handler {

	public ElizaHandler() {

	}

	public String search(IncomingRequest request) throws JsonProcessingException, IOException {
		String query = request.getRequestContent().get(0).getText();
		ElizaMain eliza = new ElizaMain();
		System.out.println("Eliza->Read Input!!");
		eliza.readScript(true, "src/main/resources/eliza/script");
		System.out.println("Before process Input Response");
		String response = eliza.processInput(query);
		System.out.println("Got Response , response= " + response);
		String responseJSON="[{ \"comment\":\""+response+"\"}]";
		return responseJSON;

	}

}
