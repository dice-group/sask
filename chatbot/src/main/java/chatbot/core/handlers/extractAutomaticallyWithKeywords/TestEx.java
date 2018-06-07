package chatbot.core.handlers.extractAutomaticallyWithKeywords;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class TestEx {
	private static Logger log = Logger.getLogger(TestEx.class.getName());

	@Autowired
//	@LoadBalanced
	    private RestCall restCall;
//	
//	public String config() {
//		
//		System.out.println("In Config");
//		String restReturn = restCall.makeRestCall();
//		return restReturn;
//	}
}
