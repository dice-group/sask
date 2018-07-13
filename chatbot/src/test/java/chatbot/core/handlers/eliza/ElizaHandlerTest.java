package chatbot.core.handlers.eliza;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

public class ElizaHandlerTest {

	private ElizaHandler testElizaObject = new  ElizaHandler();
	
	public IncomingRequest createInitialRequest(String incomingText) {
		
		IncomingRequest request =new IncomingRequest() ;
		RequestContent addingContent = new RequestContent();	
		addingContent.setPayload("text");
		addingContent.setText(incomingText);
		List<RequestContent> messageData = 	new ArrayList<RequestContent>();
		messageData.add(addingContent);
		request.setRequestContent(messageData);
		return request;
	}
	
	@Test
	public void testCreateNewResponse() {
		String request = "Hello";
		Response elizaResponse = testElizaObject.createNewResponse(request);	
		assertEquals(request, elizaResponse.getContent() );
	}
	
	@Test
	public void testSearchForCorrectInput() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hello"));
		MessageType actualType = responseListObject.getMessageType();
		String actualOutput = responseListObject.getMessageData().get(0).getContent();
		String expectedOutput = "How do you do. Please state your question.";
		String expectedOutput2 = "Hi. What seems to be your question ?";
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertNotNull(expectedOutput2);
		assertEquals("Message when failed Message Type", MessageType.PLAIN_TEXT, actualType );
		
		if (actualOutput.equalsIgnoreCase(expectedOutput) || actualOutput.equalsIgnoreCase(expectedOutput2)) {
			assert true;
		} else {
			assertEquals("Message when Incorrect Output", expectedOutput + " || " + expectedOutput2, actualOutput );
		}
	}

	@Test
	public void testCheckInputWithIncorrectInput() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hello$?"));
		String actualOutput = responseListObject.getMessageData().get(0).getContent();
		String expectedOutput = "I'm not sure I understand you fully.";
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertNotEquals("Message when Incorrect Output", expectedOutput, actualOutput );
	}
	
	@Test
	public void testCheckInputWithZeroInHello() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hell0"));
		String actualOutput = responseListObject.getMessageData().get(0).getContent();
		String expectedOutput = "I'm not sure I understand you fully.";
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertEquals("Message when failed Message testCheckInputWithZeroInHello function", expectedOutput, actualOutput );
	}
	
	@Test
	public void testCheckInputWithUnicode() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hell\u006F"));
		String actualOutput = responseListObject.getMessageData().get(0).getContent();
		String expectedOutput = "How do you do. Please state your question.";
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertEquals("Message when Incorrect Output", expectedOutput, actualOutput );
	}
		
	@Test
	public void testCheckMessageType() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hello"));
		MessageType actualType = responseListObject.getMessageType();
		assertTrue("Message when failed", MessageType.TEXT_WITH_URL != actualType );
		assertTrue("Message when failed", MessageType.PLAIN_TEXT == actualType );
	}	
}
