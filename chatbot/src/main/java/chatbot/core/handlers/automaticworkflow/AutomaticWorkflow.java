package chatbot.core.handlers.automaticworkflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;


@RestController
public class AutomaticWorkflow extends Handler {
	
	private final RestTemplate restTemplate;
	
	public AutomaticWorkflow(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private static Logger log = Logger.getLogger(AutomaticWorkflow.class.getName());

	public ResponseList search(IncomingRequest request) throws IOException {
		try {
			
			ResponseList responselist = new ResponseList();
			String query = request.getRequestContent()
			                      .get(0)
			                      .getText();
			                      
			log.warn("IN AutomaticWorkflow SEARCH:::"+query);
			
			responselist = startFunction(query);
			log.warn("RESPONSE LIST SEARCH::"+responselist);
			return responselist;

		} catch (Exception e) {
			log.error("search, Exception in handling Automatic Handling Queries");
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
					responseData.setContent("The query should be \"Extract From Filename Using Extractor\"");
					returnValue.addMessage(responseData);
					return returnValue;
				}
				
				if( !extractor.isEmpty() && !fileName.isEmpty()) {
					log.warn("IN EXCT AND FILE NT EMPTY");
					returnValue = checkFilePresent(extractor, fileName);
										
				} else {
					log.warn("QUERY EXTRACT STRING IN ELSE");
					responseData.setContent("UNKNOWN EXCEPTION FROM ELSE");
					returnValue.addMessage(responseData);
				}
		}
//			else if(splitQuery[0].toLowerCase().equals("upload") ) {
//			returnValue = uploadFile();
//			
//		}
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

		return fileInfo; 
	}
	
	public ResponseList constructWorkFlow(String extractor,String fileName) {
		ResponseList fileInfo =  new ResponseList();
		Response responseData = new Response();
		responseData.setContent("RETURN CONSTRUCTWF FUNC");
		log.warn("IN CONSTRUCT WORKFLOW");

		/*
		 * create
		 */
		// o1		
		Map<String, String> outputs1 = new HashMap<>();	

		outputs1.put("output_55uqv2wzcus", "NL");
		outputs1.put("output_y97vayejxre", "NL");

		Operator o1 = new Operator();
		o1.setType("file");
		o1.setId("node_hgibcp02st");
		o1.setContent("/"+fileName);
		o1.setOutputs(outputs1);
		
		Map<String, Operator> op1 = new HashMap<>();
		op1.put("node_hgibcp02st", o1);
		
		
		// o2
		Map<String, String> inputs2 = new HashMap<>();
		
		inputs2.put("input_tzil25otre", "NL");
		inputs2.put("input_2hfqgk71ukp", "NL");

		Map<String, String> outputs2 = new HashMap<>();

		outputs2.put("output_a2b3epd6nd", "RDF");
		outputs2.put("output_09phmd5hdmvi", "RDF");

		Operator o2 = new Operator();
		o2.setType("extractor");
		o2.setId("node_178rj2s179x");
		o2.setContent("FOX-MS");
		
		o2.setInputs(inputs2);
		o2.setOutputs(outputs2);
		
		Map<String, Operator> op2 = new HashMap<>();
		op1.put("node_178rj2s179x", o2);
		log.warn("O2 To String:::"+o2.toString());
		
	
		// o3
		Map<String, String> inputs3 = new HashMap<>();

		inputs3.put("input_5ezhp221vou", "RDF");
		inputs3.put("input_g4z7qll7d8s", "RDF");
			
		Operator o3 = new Operator();
		o3.setType("db");
		o3.setId("node_hcj9pytiiml");
		o3.setContent("sask");
		o3.setInputs(inputs3);
	
		Map<String, Operator> op3 = new HashMap<>();
		op3.put("node_hcj9pytiiml", o3);
		log.warn("O3 To String:::"+o3.toString());

		
		// l1
		Link l1 = new Link();
		l1.setFromOperator("node_hgibcp02st");
		l1.setToOperator("node_178rj2s179x");
		l1.setFromConnector("output_55uqv2wzcus");
		l1.setToConnector("input_tzil25otre");

		// l2
		Link l2 = new Link();
		l2.setFromOperator("node_178rj2s179x");
		l2.setToOperator("node_hcj9pytiiml");
		l2.setFromConnector("output_a2b3epd6nd");
		l2.setToConnector("input_5ezhp221vou");	

		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("node_hgibcp02st", o1);
		w.getOperators().put("node_178rj2s179x", o2);
		w.getOperators().put("node_hcj9pytiiml", o3);
		
		w.getLinks().add(l1);
		w.getLinks().add(l2);
				
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
				
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Workflow.class, new WorkflowSerializer());
		mapper.registerModule(module);
		try {
			String jsonInString = mapper.writeValueAsString(w);
			
			log.warn("jsonInString..."+jsonInString);
			log.warn("BEFOR REST CALL***");

			String uri1 = "http://EXECUTER-MS/executeWorkflow";
		
			HttpEntity<String> request = new HttpEntity<String>(jsonInString, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(uri1, request, String.class );
		
			log.warn("Response Code::."+response.getStatusCodeValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			responseData.setContent("done");
			fileInfo.addMessage(responseData);	

	return fileInfo;
	}
	
}