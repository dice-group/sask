/**
 * 
 */
package chatbot.core.handlers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Prashanth
 *
 */
abstract public class Handler {
	
	public String search(String question) throws JsonProcessingException, IOException{
		return null;
	}

}
