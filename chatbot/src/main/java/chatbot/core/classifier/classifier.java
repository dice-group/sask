/**
 * 
 */
package chatbot.core.classifier;
import chatbot.core.handlers.qa.*;
import chatbot.core.handlers.*;

/**
 * @author Prashanth
 * class to Classify the User Input as QA or KS or Normal emotion.
 */
public class classifier {
	
	//Classify Intent here. Need another function to process Requests. Then finally Search to the correct class. 
	//One idea would be to have polymorphism here with similar interfaces but different logic for Keyword search and QA. 
	//But is that an outdated way to code?
	public Handler classify(String question) throws Exception { //Modify later to throw specialized exception rather than the generic one.
		try {
		
		//If rivescript 
		//TODO
		//else if QA
		Handler obj = new qa(); //Direct Hardcoding to check QA for now.//obj.search(question);
		//else if KS
		
		//else //ELIZA/ALICE?
		
		//obj.search(question);
		return obj;
		
	}
	catch(Exception e) {
		System.out.println(e.getStackTrace());
		throw e;
	}
	}

}
