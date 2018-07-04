package chatbot.core.handlers.automaticworkflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chatbot.core.handlers.Handler;
import chatbot.core.handlers.rivescript.RiveScriptOutputAnalyzer;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;

//@RestController
public class AutomaticWorkflow extends Handler {
	
	private final RestTemplate restTemplate;
	
	public AutomaticWorkflow(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private static Logger log = Logger.getLogger(AutomaticWorkflow.class.getName());

	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			// Analyze passed Output from Rivescript for whether further
			// processing is required.
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			                      
			log.warn("IN AutomaticWorkflow SEARCH:::"+query);
			
			responselist = startFunction(query);
			log.warn("RESPONSE LIST SEARCH::"+responselist);
			return responselist;

		} catch (Exception e) {
			// Check if we can create a logger.
			log.error("search, Exception in handling Rivescript Queries");
			ResponseList responselist = new ResponseList();
			responselist.setError();
			return responselist;
		}

	}
	
	public ResponseList startFunction(String query) {

		int queryLength = query.trim().split("\\s+").length;
		log.warn("Start Func Query:"+query);
		log.warn("Start Func Query Length:"+query.trim().split("\\s+").length);

		String[] splitQuery = query.trim().split(" ");
		log.warn("Start Func Query Equals:"+splitQuery[0].toLowerCase().equals("extract"));
		
		ResponseList returnValue =  new ResponseList();
		log.warn("splitQuery.length::"+(splitQuery.length==1));
		Response responseData = new Response();
		String keyword = null;
		String extractor = null;
		String fileName = null;
		
		
		
//		log.info("Compare length::"+ (queryLength > 1));
//		log.info("Compare length Equal::"+ (queryLength == 1));
//		 if(splitQuery[0].toLowerCase().equals("extract") && splitQuery.length==1 ){
//			log.info("QUERY EXTRACT STRING EMPTY");
//			responseData.setContent("The query should be \"extract extractor filename\"");
//			returnValue.addMessage(responseData);
//		}else 
			if (splitQuery[0].toLowerCase().equals("extract") ) {
				log.warn("QUERY EXTRACT::"+splitQuery.length);
				if(splitQuery.length==5) {
					keyword = splitQuery[0];
					fileName = splitQuery[2];
					extractor = splitQuery[4];
					log.warn("keyword"+keyword);
					log.warn("extractor"+extractor);
					log.warn("fileName"+fileName);
				}else {
					responseData.setContent("The query should be \"extract From filename using extractor\"");
					returnValue.addMessage(responseData);
					return returnValue;
				}
				
				if( !extractor.isEmpty() && !fileName.isEmpty()) {
					returnValue = checkFilePresent(extractor, fileName);
				} else {
					log.warn("QUERY EXTRACT STRING IN ELSE");
					responseData.setContent("UNKNOWN EXCEPTION");
					returnValue.addMessage(responseData);
				}
		} else if(splitQuery[0].toLowerCase().equals("upload") ) {
			returnValue = uploadFile();
			
		}
		return returnValue;
		
	}

	public ResponseList checkFilePresent(String extractor, String fileName) {
		ResponseList fileInfo =  new ResponseList();
		Response responseData = new Response();
		log.warn("In extractWF");
		log.warn("extractor:::"+extractor);	
		log.warn("fileName:::"+fileName);
		
		String uri = "http://REPO-MS/getHdfsStructure?location=repo";
  		String response = restTemplate.getForObject(uri, String.class);
  		
  		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jsonInputText = jelement.getAsJsonObject();
  		log.warn("jsonInputText:"+jsonInputText);

		JsonArray typeValue = jsonInputText.getAsJsonArray("fileList");
  		log.warn("typeValue:"+typeValue);
  		log.warn("typeValueSize:"+typeValue.size());
  		List<String> fileList = new ArrayList<String>();

  		for(int i = 0; i<typeValue.size(); i++) {
  			 JsonObject objectTest = typeValue.get(i).getAsJsonObject();
  			 String receivedFileName = objectTest.get("suffix").getAsString();
  			fileList.add(receivedFileName);
  		}
			 log.warn("filename:"+fileList);
			 if(fileList.contains(fileName)) {
				 log.warn("FILE PRESENT");	 
				 fileInfo= constructWorkFlow(extractor,fileName);
			 } else {
				 log.warn("FILE NOT PRESENT");
				 responseData.setContent("Please enter the correct file name");
				 fileInfo.addMessage(responseData);
			 }
  		log.warn("After search rest call");

		return fileInfo; 
	}
	
	public ResponseList constructWorkFlow(String extractor,String fileName) {
		ResponseList fileInfo =  new ResponseList();
		Response responseData = new Response();
		responseData.setContent("RETURN CONSTRUCTWF FUNC");
		log.warn("IN CONSTRUCT WORKFLOW");
		log.warn("EXTRACTOR::"+extractor);
		log.warn("FILENAME::"+fileName);

		Workflow workflow = new Workflow();
		
		Link link = new Link();
		link.setFromConnector("A");
		link.setToConnector("B");
		link.setFromOperator("id_1");
		link.setFromConnector("id_2");
		
		workflow.getLinks().add(link);
		
		Operator id_1 = new Operator();
		id_1.setId("id_1");
		id_1.setContent(fileName);
		id_1.setType("file");
		id_1.getOutputs().put("A", "NL");
		workflow.getOperators().put("id_1", id_1);
		
		log.warn("1::"+workflow.getLinks().get(0).getFromConnector());
		log.warn("2::"+workflow.getOperators().get("id_1").getId());
		log.warn("3::"+workflow.getOperators().get("id_1").getOutputs().get("A"));
		
		Operator id_2 = new Operator();
		id_2.setId("id_2");
		id_2.setContent(extractor);
		id_2.setType("extractor");
		id_2.getInputs().put("B", "RDF");
		workflow.getOperators().put("id_2", id_2);
		
		
		log.warn("4::"+workflow.getLinks().get(0).getFromConnector());
		log.warn("5::"+workflow.getOperators().get("id_2").getId());
		log.warn("6::"+workflow.getOperators().get("id_2").getContent());
		log.warn("7::"+workflow.getOperators().get("id_2").getInputs().get("B"));
		
//		String uri = "http://EXECUTER-MS/executeSimple?data=%2FtestData.txt&extractor=OPEN-IE-MS&targetGraph=sask";
		String uri = "http://EXECUTER-MS/executeSimple";
		log.warn("Before rest call");
		String response = restTemplate.getForObject(uri, String.class, workflow);
		log.warn("CONSTRUCT WORKFLOW RESPONSE::"+response );
		responseData.setContent(response);
		fileInfo.addMessage(responseData);
	return fileInfo;
	}
	
	public ResponseList uploadFile() {
		ResponseList fileInfo =  new ResponseList();
		Response responseData = new Response();
		responseData.setContent("RETURN UPLOADWF FUNC");
		log.warn("IN CONSTRUCT WORKFLOW");
		String uri = "http://REPO-MS/storeFile";
		String response = restTemplate.getForObject(uri, String.class);
		log.warn("Upload WF::"+response );
		responseData.setContent(response);
		fileInfo.addMessage(responseData);
	return fileInfo;
	}
}


//Repo-microservice storeFile() invoked
//: http://localhost:50070/webhdfs/v1/user/DICE/repo/notes%20of%20presntn.txt?op=CREATE&overwrite=true
//: Repo-microservice getHdfsStructure() invoked
//: http://localhost:50070/webhdfs/v1/user/DICE/repo/?op=LISTSTATUS


