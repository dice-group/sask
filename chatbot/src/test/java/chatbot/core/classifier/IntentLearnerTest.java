package chatbot.core.classifier;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import chatbot.core.handlers.eliza.ElizaHandler;
import chatbot.core.handlers.qa.QAHandler;
import chatbot.core.handlers.rivescript.RiveScriptQueryHandler;
import chatbot.core.handlers.sessa.SessaHandler;
import chatbot.io.incomingrequest.FeedbackRequest;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;

public class IntentLearnerTest {
	
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
	
	public Object classifyInput(IncomingRequest input) {
		IntentLearner intentLeaner = new IntentLearner();
		Object actualOutput =intentLeaner.classify(input);	
		return actualOutput;
	}

	@Test
	public void testClassifyForRiveScript() {

		IncomingRequest input = createInitialRequest("Hello");
		Object actualOutput= classifyInput(input);
        assertTrue(actualOutput instanceof RiveScriptQueryHandler);
	}
	
	@Test
	public void testClassifyForQAHandler() {
		
		IncomingRequest input = createInitialRequest("what is Obama's birthplace");
		Object actualOutput= classifyInput(input);
        assertTrue(actualOutput instanceof QAHandler);
	}
	
	@Test
	public void testClassifyForElizaHandler() {

		IncomingRequest input = createInitialRequest("I want to know something");
		Object actualOutput= classifyInput(input);
        assertTrue(actualOutput instanceof ElizaHandler);
	}

	@Test
	public void testClassifyForSessaHandler() {
		
		IncomingRequest input = createInitialRequest("obama wife");
		Object actualOutput= classifyInput(input);
        assertTrue(actualOutput instanceof SessaHandler);
	}
	
	@AfterClass
	public static void tearDown() {
		FeedbackRequest feedback = new FeedbackRequest();
		feedback.setFeedback("negative");
		feedback.setQuery("obama wife");
		IntentLearner intentLeaner = new IntentLearner();
		intentLeaner.processFeedback(feedback);
	}
}
