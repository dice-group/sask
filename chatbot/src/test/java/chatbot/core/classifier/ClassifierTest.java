package chatbot.core.classifier;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import chatbot.core.handlers.eliza.ElizaHandler;
import chatbot.core.handlers.qa.QAHandler;
import chatbot.core.handlers.rivescript.RiveScriptQueryHandler;
import chatbot.core.handlers.sessa.SessaHandler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.incomingrequest.RequestContent;

public class ClassifierTest {
	
	Logger log = Logger.getLogger(ClassifierTest.class.getName());

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
		Classifier helper = new Classifier();
		Object actualOutput =helper.classify(input);	
		return actualOutput;
	}

	@Test
	public void testClassifyForRiveScript() {

		IncomingRequest input = createInitialRequest("Hello");
		Object actualOutput= classifyInput(input);
		log.info("Executing RiveScript Test");
        assertTrue(actualOutput instanceof RiveScriptQueryHandler);
	}
	
	@Test
	public void testClassifyForQAHandler() {
		
		IncomingRequest input = createInitialRequest("why ?");
		Object actualOutput= classifyInput(input);
		log.info("Executing QAHandle Test");
        assertTrue(actualOutput instanceof QAHandler);
	}
	
	@Test
	public void testClassifyForElizaHandler() {

		IncomingRequest input = createInitialRequest("Me");
		Object actualOutput= classifyInput(input);
		log.info("Executing Eliza Test");
        assertTrue(actualOutput instanceof ElizaHandler);
	}

	@Test
	public void testClassifyForSessaHandler() {
		
		IncomingRequest input = createInitialRequest("wq");
		Object actualOutput= classifyInput(input);
		log.info("Executing SessaHandler Test");
        assertTrue(actualOutput instanceof SessaHandler);
	}
}
