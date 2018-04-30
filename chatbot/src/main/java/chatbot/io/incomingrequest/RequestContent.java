/**
 * 
 */
package chatbot.io.incomingrequest;

/**
 * @author Prashanth
 *
 */
public class RequestContent {
	private String data;
	private String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String content) {
		payload = content;
	}

	public String getText() {
		return data;
	}

	public void setText(String text) {
		data = text;
	}
}
