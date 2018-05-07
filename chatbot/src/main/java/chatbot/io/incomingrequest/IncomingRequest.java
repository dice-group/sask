/**
 * 
 */
package chatbot.io.incomingrequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Prashanth
 *
 */
public class IncomingRequest {
	public enum MessageType {
		TEXT, OTHERS
	};

	private String userId;
	private MessageType messageType;
	private List<RequestContent> requestContent = new ArrayList<RequestContent>();
	private String client;

	public IncomingRequest() {
	}

	public IncomingRequest(String id, String type, String platform) {
		userId = id;
		if (type.compareTo("text") == 0)
			messageType = MessageType.TEXT;
		else
			messageType = MessageType.OTHERS;
		client = platform;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String platform) {
		client = platform;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String id) {
		userId = id;
	}

	public List<RequestContent> getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(List<RequestContent> messageData) {
		requestContent = messageData;
	}

	public void setMessageType(String type) {
		if (type.compareTo("text") == 0)
			messageType = MessageType.TEXT;
		else
			messageType = MessageType.OTHERS;
	}

	public MessageType getMessageType() {
		return messageType;
	}
}
