package chatbot.core.handlers.eliza;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.qos.logback.core.net.SyslogOutputStream;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

public class ElizaHandlerTest {
	private static Logger log = Logger.getLogger(ElizaHandler.class.getName());

	ElizaHandler testElizaObject = new  ElizaHandler();
	
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
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertEquals("Message when failed Message Type", MessageType.PLAIN_TEXT, actualType );
		assertEquals("Message when Incorrect Output", expectedOutput, actualOutput );
	}

	@Test
	public void testCheckInputWithIncorrectInput() throws JsonProcessingException, IOException {
		
		ResponseList responseListObject =  testElizaObject.search(createInitialRequest("Hello$?"));
		MessageType actualType = responseListObject.getMessageType();
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
		System.out.println(actualOutput);
		System.out.println(expectedOutput);
		assertNotNull(actualOutput);
		assertNotNull(expectedOutput);
		assertEquals("Message when failed Message Type", expectedOutput, actualOutput );
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
		System.out.println(responseListObject.getMessageType());
		MessageType actualType = responseListObject.getMessageType();
		assertTrue("Message when failed", MessageType.TEXT_WITH_URL != actualType );
		assertTrue("Message when failed", MessageType.PLAIN_TEXT == actualType );
	}	
}