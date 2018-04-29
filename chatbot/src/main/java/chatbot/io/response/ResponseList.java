/**
 * 
 */
package chatbot.io.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Prashanth
 * * Base Response class. 
 * Due to "What can you do" which returns various Buttons , an Array List of this class is to be returned.
 * Due to What can you do returning various options for each card, an array list internally of URL's have to be maintained.
 */
public class ResponseList {
	private List<Response> messageData = new ArrayList<>();
	public enum MessageType {
		PLAIN_TEXT,URL,TEXT_WITH_URL,FEEDBACK_REQUEST,OTHERS
	}; //URL is temporary till we come up with reading and extracting heading from content.
	
	private MessageType messageType=MessageType.PLAIN_TEXT;
	private boolean isError = false;
	public ResponseList(){
		isError=false;
		messageType=MessageType.PLAIN_TEXT;
	}
	public List<Response> getMessageData() {
	   return messageData;
	}
	public void setMessageData(List<Response> messageData) {
	   this.messageData = messageData;
    }
	public void addMessage(Response responseData) {
        messageData.add(responseData);
    }
	public void setError() {
		isError = true;
	}
	public boolean isError() {
		return isError;
	}
	public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType type) {
        messageType = type;
    }
	
}
