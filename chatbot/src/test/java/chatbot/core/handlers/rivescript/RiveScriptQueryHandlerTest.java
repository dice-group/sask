package chatbot.core.handlers.rivescript;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rivescript.RiveScript;

import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;

public class RiveScriptQueryHandlerTest {

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
	
	public RiveScript createInitialRequest() {
			RiveScript obj = new RiveScript();
			obj.loadDirectory("src/main/resources/rivescript");
			obj.sortReplies();
		return obj;
	}
	
	public RiveScriptQueryHandler createInitialObject() {
		RiveScriptQueryHandler queryHandlerObject = new RiveScriptQueryHandler();
		return queryHandlerObject;
	}
	
	/**
	 * Test for checking query pass and fail scenario
	 */
	@Test
	public void testSearchHi() throws IOException {
		IncomingRequest input = createIncomingRequest("Hi");
		RiveScriptQueryHandler queryObject = createInitialObject();
		String actualOutput = queryObject.search(input).getMessageData().get(0).getContent();
		List<String> expectedOutputs = Arrays.asList("Hi, how may I help you?", "Hello there", "Hi to you too!", "Hello, how may I help you?");
		assertNotNull(actualOutput);
		boolean result = expectedOutputs.contains(actualOutput);
		assertTrue(result);			
	}
	
	/**
	 * Testing Day Greetings with Unicode of o
	 */
	@Test
	public void testSearchDayGreetings() throws IOException {
		IncomingRequest input = createIncomingRequest("Go\u006Fd Morning");
		RiveScriptQueryHandler queryObject = createInitialObject();
		String actualOutput = queryObject.search(input).getMessageData().get(0).getContent();
		List<String> expectedOutputs = Arrays.asList("good morning to you too", "good evening to you too", "good night to you too");
		assertNotNull(actualOutput);
		boolean result = expectedOutputs.contains(actualOutput);
		assertTrue(result);			
	}
	
	@Test
	public void testSearch() throws IOException {
		IncomingRequest input = createIncomingRequest("how are you?");
		RiveScriptQueryHandler queryObject = createInitialObject();
		String actualOutput = queryObject.search(input).getMessageData().get(0).getContent();
		List<String> expectedOutputs = Arrays.asList("Never been better", "I'm good, you?", "I am fine thanks for asking", "I'm fine, thanks for asking!", "I'm great, how are you?", "Good :) you?", "Great! You?");
		assertNotNull(actualOutput);
		boolean result = expectedOutputs.contains(actualOutput);
		assertTrue(result);
		}

	@Test
	public void testSearchDbpedia() throws IOException {
		IncomingRequest input = createIncomingRequest("who are you?");
		RiveScriptQueryHandler queryObject = createInitialObject();
		String actualOutput = queryObject.search(input).getMessageData().get(0).getContent();
		String expectedHelloOutput = "I am the DBpedia Bot";
		assertNotNull(actualOutput);
		assertEquals(expectedHelloOutput, actualOutput);		
	}	
	
	@Test
	public void testSearchBye() throws IOException {
		IncomingRequest input = createIncomingRequest("Bye");
		RiveScriptQueryHandler queryObject = createInitialObject();
		String actualOutput = queryObject.search(input).getMessageData().get(0).getContent();
		List<String> expectedOutputs = Arrays.asList("See ya!", "Bye","Good Bye");
		assertNotNull(actualOutput);
		boolean result = expectedOutputs.contains(actualOutput);
		assertTrue(result);			
	}
}
