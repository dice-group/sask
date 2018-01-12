/**
 * 
 */
package chatbot.io.response;

/**
 * @author Prashanth
 *
 */
public class EntryInformation {
	public enum Type {
		TEXT, URL,OTHERS
	};
	private String displayText;
    private Type buttonType; //Maybe not required in current context, Will consider removing.
    private String uri;
    public EntryInformation() {
    		displayText="";
    		uri="";
    		buttonType=Type.TEXT;
    }
    public EntryInformation(String title, Type type, String url) {
    		displayText = title;
        buttonType = type;
        uri = url;
    }
    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String title) {
    		displayText = title;
    }

    public Type getButtonType() {
        return buttonType;
    }

    public void setButtonType(Type type) {
        buttonType = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String url) {
        uri = url;
    }
}
