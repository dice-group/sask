/**
 * 
 */
package chatbot.core.handlers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import chatbot.core.IncomingRequest.IncomingRequest;

/**
 * @author Prashanth
 *
 */
abstract public class Handler {
	
	public String search(IncomingRequest request) throws JsonProcessingException, IOException{
		return null;
	}

}
