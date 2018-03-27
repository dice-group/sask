package chatbot.core.handlers.rivescript;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.rivescript.RiveScript;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;
import chatbot.io.response.ResponseList;

public class RiveScriptQueryHandlerTest {

	private static Logger log = Logger.getLogger(RiveScriptQueryHandlerTest.class.getName());

	public IncomingRequest createIncomingRequest(String incomingText) {	
		IncomingRequest request =new IncomingRequest() ;
		RequestContent addingContent = new RequestContent();	
		addingContent.setPayload("text");
		addingContent.setText(incomingText);
		List<RequestContent> messageData = 	new ArrayList<RequestContent>();
		messageData.add(addingContent);
		request.setRequestContent(messageData);
		return request;
	}	
	
	public RiveScript CreateInitialRequest() {
			RiveScript obj = new RiveScript();
			obj.loadDirectory("src/main/resources/rivescript");
			obj.sortReplies();
		return obj;
	}
	
	public RiveScriptQueryHandler createInitialObject() {
		RiveScriptQueryHandler queryHandlerObject = new RiveScriptQueryHandler();
		return queryHandlerObject;
	}
	
	public boolean queryFound(String query){
		
		RiveScriptQueryHandler queryObject = createInitialObject();
		boolean actualQueryHandlerOutput = queryObject.isQueryFound(query);
		return actualQueryHandlerOutput;
	}	
	/**
	 * Test for checking query pass and fail scenario
	 */
	@Test
	public void testIsQueryFound() {		
		String queryTrue = "Hello";
		String queryFalse= "Something Random";
		boolean replyTrue = queryFound(queryTrue);
		boolean replyFalse = queryFound(queryFalse);
		log.info("Executing RiveScript");		
		assertTrue(replyTrue);
		assertFalse(replyFalse);
		}

	@Test
	public void testSearch() throws IOException {
		IncomingRequest input = createIncomingRequest("HELLO");
		RiveScriptQueryHandler queryObject = createInitialObject();
		ResponseList actualOutput = queryObject.search(input);
//		Array test = ["Hi, how may I help you?, "Hello there"];
		String[] expcectedHelloOutput = {"Hi, how may I help you?", "Hello there", "Hi to you too!"};
		System.out.println("In Search");
		assertNotNull(actualOutput);
		System.out.println(actualOutput.getMessageData().get(0).getContent());
		for(int i=0; i < expcectedHelloOutput.length; i++) {
			if(expcectedHelloOutput[i]== actualOutput.toString())
				break;

		}
//		assertEquals(expcectedHelloOutput, actualOutput);
		
//		ResponseList testResponselist1 = new ResponseList();
//      Returns the input text from the object
//		String query = input.getRequestContent()
//		                      .get(0)
//		                      .getText()
//		                      .toLowerCase();
//		System.out.println(query);

//		Checks incoming input in rivescript to find a matching response		
//		String reply = CreateInitialRequest().reply("user", query);
//		System.out.println("Test reply : "+reply);
		
//		String reply = obj.reply("user", query);
//		RiveScriptOutputAnalyzer analyzer = new RiveScriptOutputAnalyzer();
//		responselist = analyzer.HandleTextMessage(responselist , reply);
//        assertTrue(actualOutput instanceof RiveScriptQueryHandler);

	}
}
