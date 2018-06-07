package chatbot.core.handlers.extractAutomaticallyWithKeywords;

import java.io.IOException;

import org.apache.log4j.Logger;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ExtractAutomaticallyWithKeywordsHandler extends Handler{
	private static Logger log = Logger.getLogger(ExtractAutomaticallyWithKeywordsHandler.class.getName());

	private final RestTemplate restTemplate;

	public ExtractAutomaticallyWithKeywordsHandler (RestTemplate restTemplate) {
		log.info("Return from Constructor");
		this.restTemplate = restTemplate;
}

	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			log.info("In Search REQUEST");
//		  	String uri = "http://REPO-MS/getHdfsStructure?location=repo";
			String uri = "http://INTELLIGENTDATAASSISTANT/executeIntelligentDataAssistant";
		  	log.info("Before search rest call");
	  		String response = restTemplate.getForObject(uri, String.class);	
	  		log.info("RESPONSE:"+response);
	  		log.info("After search rest call");

			ResponseList responselist = new ResponseList();	
			return responselist;

		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("EXTRACT AUTOMATICALLY, Exception in handling AUTOMATIC EXTRACTION Queries");
			ResponseList responselist = new ResponseList();
			responselist.setError();
			log.info(e.getMessage());
			return responselist;
		}
	}
}

//public String extractFileNames() {
////	{"suffix":"","path":"","type":"DIRECTORY","fileList":[{"suffix":"OutputAnalyzerTest.txt","path":"/OutputAnalyzerTest.txt","type":"FILE","fileList":[]},{"suffix":"test.txt","path":"/test.txt","type":"FILE","fileList":[]},{"suffix":"testData.txt","path":"/testData.txt","type":"FILE","fileList":[]}]}	  		
//
////	 {"path":"","suffix":"","type":"DIRECTORY","fileList":[{"path":"/OutputAnalyzerTest.txt","suffix":"OutputAnalyzerTest.txt","type":"FILE","fileList":"[]"}]}
//	try {
//		log.info("IN EXC FUNC");
//
//		JSONObject json = new JSONObject();
//		json.put("suffix", "");
//		json.put("path", "");
//		json.put("type", "DIRECTORY");
//	
//		JSONArray array = new JSONArray();
//		JSONObject item = new JSONObject();
//		item.put("suffix", "OutputAnalyzerTest.txt");
//		item.put("path","/OutputAnalyzerTest.txt");
//		item.put("type", "FILE"); 
//		item.put("fileList", "[]");
//		array.put(item);
//		json.put("fileList", array);
//		
//		String message = json.toString();
//		return message;
//	}
//	 catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	 	}
//	return null;
//	}
