package chatbot.core.handlers.rivescript;

import static org.junit.Assert.*;
import org.apache.log4j.Logger;
import chatbot.io.response.ResponseList;
import org.json.JSONObject;
import org.junit.Test;

public class RiveScriptOutputAnalyzerTest {

		Logger log = Logger.getLogger(RiveScriptOutputAnalyzerTest.class.getName());
		
//		public Boolean checkJsonType(String textMessage) {
//				RiveScriptOutputAnalyzer analyseOutput = new RiveScriptOutputAnalyzer();
//				Boolean checkValue =  analyseOutput.isJSONObject(textMessage);
//				return checkValue;
//		}
		
//		@Test
//		public void	testIsJSONObject() {
//			String inputMessage = "[{\"foo\": \"bar\"},{\"foo\": \"biz\"}]";
//			Boolean checkVal = checkJsonType("Hello"); //requestContent:[{text: "hello"}]
//			Boolean checkVal2 = checkJsonType(inputMessage); //requestContent:[{text: "hello"}]
//
////			Method returns false when input has JSON object and not string
////			assertFalse(checkVal);
//			assertTrue(checkVal2);
//
//		}
		
//		@Test
//		public void	testIsJSONHasType(String textMessage) {
//			JSONObject jsonResult = new JSONObject(textMessage);
//			String typeValue = jsonResult.getString("name");
////			assertTrue(checkValue);
//			assertEquals("help", typeValue);
//		}	
//		
//		@Test
//		public void	testJSONHasNoType(ResponseList responselist) {
//			JSONObject jsonResult = new JSONObject();//pass of object here
//			String typeValue = jsonResult.getString("name");
//			assertEquals("help", typeValue);
//		}	
//		
////		@Test
////		public void testHandleTextMessage(ResponseList responselist,String textMessage ) {
////
////		
////		}
//
	}

