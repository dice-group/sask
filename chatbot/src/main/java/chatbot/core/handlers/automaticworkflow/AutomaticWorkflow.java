package chatbot.core.handlers.automaticworkflow;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dice_research.sask_commons.workflow.Link;
import org.dice_research.sask_commons.workflow.Operator;
import org.dice_research.sask_commons.workflow.Workflow;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import chatbot.core.handlers.Handler;
import chatbot.core.handlers.rivescript.RiveScriptOutputAnalyzer;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;


@RestController
public class AutomaticWorkflow extends Handler {
	
	private final RestTemplate restTemplate;
//	@Autowired
//	@LoadBalanced
//	protected RestTemplate restTemplate;
	
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
					log.warn("IN EXCT AND FILE NT EMPTY");
					returnValue = checkFilePresent(extractor, fileName);
					
//						returnValue = constructWorkFlow(extractor, fileName);
					
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
//  		log.warn("After search rest call");

		return fileInfo; 
	}
	
//	public ResponseList constructWorkFlow(String extractor,String fileName) {
//		ResponseList fileInfo =  new ResponseList();
//		Response responseData = new Response();
//
//		log.warn("IN TEST REQUEST");
//		String uri = "http://EXECUTER-MS/test";
//  		String response = restTemplate.postForObject(uri, "test",String.class);
//		
//		responseData.setContent(response);		
//		fileInfo.addMessage(responseData);;
//		return fileInfo;
//	}
	public ResponseList constructWorkFlow(String extractor,String fileName) {
		ResponseList fileInfo =  new ResponseList();
		Response responseData = new Response();
		responseData.setContent("RETURN CONSTRUCTWF FUNC");
		log.warn("IN CONSTRUCT WORKFLOW");
//		log.warn("EXTRACTOR::"+extractor);
//		log.warn("FILENAME::"+fileName);

		Map<String, String> outputs1 = new HashMap<>();
		outputs1.put("id1_1", "NL");
		
		Operator o1 = new Operator();
		o1.setContent("/testData.txt");
		o1.setId("id1");
		o1.setType("file");
		o1.setOutputs(outputs1);
		
		// o2
		Map<String, String> inputs2 = new HashMap<>();
		inputs2.put("id2_1", "NL");
		
		Map<String, String> outputs2 = new HashMap<>();
		outputs2.put("id2_1", "RDF");
		
		Operator o2 = new Operator();
		o2.setContent("FOX-MS");
		o2.setId("id2");
		o2.setType("extractor");
		o2.setInputs(inputs2);
		o2.setOutputs(outputs2);
		
		// o3
		Map<String, String> inputs3 = new HashMap<>();
		inputs3.put("id3_1", "RDF");
		
		Operator o3 = new Operator();
		o3.setContent("sask");
		o3.setId("id3");
		o3.setType("db");
		o3.setInputs(inputs3);
		
		// l1
		Link l1 = new Link();
		l1.setFromOperator("id1");
		l1.setToOperator("id2");
		l1.setFromConnector("id1_1");
		l1.setToConnector("id2_1");
		
		// l2
		Link l2 = new Link();
		l2.setFromOperator("id2");
		l2.setToOperator("id3");
		l2.setFromConnector("id2_1");
		l2.setToConnector("id3_1");
		
		// workflow
		Workflow w = new Workflow();
		w.getOperators().put("id1", o1);
		w.getOperators().put("id2", o2);
		w.getOperators().put("id3", o3);
		w.getLinks().add(l1);
		w.getLinks().add(l2);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

//		Gson gson = new Gson();
//        String json = gson.toJson(staff);
		String rewe =  "{\"operators\":{\"node_hgibcp02st\":{\"top\":20,\"left\":100,\"properties\":{\"type\":\"file\",\"id\":\"node_hgibcp02st\",\"content\":\"/testData.txt\",\"title\":\"testData.txt\",\"inputs\":{},\"outputs\":{\"output_55uqv2wzcus\":{\"label\":\"NL\"},\"output_y97vayejxre\":{\"label\":\"NL\"}}}},\"node_178rj2s179x\":{\"top\":0,\"left\":300,\"properties\":{\"type\":\"extractor\",\"id\":\"node_178rj2s179x\",\"content\":\"FOX-MS\",\"title\":\"FOX\",\"inputs\":{\"input_tzil25otre\":{\"label\":\"NL\"},\"input_2hfqgk71ukp\":{\"label\":\"NL\"}},\"outputs\":{\"output_a2b3epd6nd\":{\"label\":\"RDF\"},\"output_09phmd5hdmvi\":{\"label\":\"RDF\"}}}},\"node_hcj9pytiiml\":{\"top\":0,\"left\":480,\"properties\":{\"type\":\"db\",\"id\":\"node_hcj9pytiiml\",\"content\":\"sask\",\"title\":\"sask\",\"inputs\":{\"input_5ezhp221vou\":{\"label\":\"RDF\"},\"input_g4z7qll7d8s\":{\"label\":\"RDF\"}},\"outputs\":{}}}},\"links\":{\"0\":{\"fromOperator\":\"node_hgibcp02st\",\"fromConnector\":\"output_55uqv2wzcus\",\"fromSubConnector\":0,\"toOperator\":\"node_178rj2s179x\",\"toConnector\":\"input_tzil25otre\",\"toSubConnector\":0},\"1\":{\"fromOperator\":\"node_178rj2s179x\",\"fromConnector\":\"output_a2b3epd6nd\",\"fromSubConnector\":0,\"toOperator\":\"node_hcj9pytiiml\",\"toConnector\":\"input_5ezhp221vou\",\"toSubConnector\":0}},\"operatorTypes\":{}}";
		log.warn("REWE::"+rewe);
		HttpEntity<String> request = new HttpEntity<String>(rewe,headers);
		log.warn("BEFOR REST CALL");
		String uri1 = "http://EXECUTER-MS/executeWorkflow";
		
		ResponseEntity<String> response = restTemplate.postForEntity(uri1, request , String.class );
		
//		Workflow workflow = new Workflow();
////		HttpEntity<Workflow> request = new HttpEntity<>(workflow);
//		
//		Link links = new Link();
//
//		links.setFromConnector("A");
//		links.setToConnector("B");
//		links.setFromOperator("id_1");
//		links.setToOperator("id_2");
//		workflow.getLinks().add(links);
//		log.warn("Link to string::"+links.toString());
//		log.warn("GET FROM CONNECTOR::"+links.getFromConnector());
		
//		Operator id_1 = new Operator();
//		Map<String, String> outputs;
		
//		id_1.setId("id_1");
//		id_1.setContent("testData.txt");
//		id_1.setType("file");
//		id_1.getOutputs().put("A", "NL");
//		workflow.getOperators().put("id_1", id_1);
//		workflow.getNextOperators(id_1);

//		log.warn("ID !::"+id_1.toString());
//		log.warn("GET OUTPT::"+id_1.getOutputs().toString());
//		log.warn("connector::"+workflow.getLinks().get(0).getFromConnector());
//		log.warn("ID::"+workflow.getOperators().get("id_1").getId());
//		log.warn("Filename::"+workflow.getOperators().get("id_1").getContent());
//		log.warn("File::"+workflow.getOperators().get("id_1").getType());
//		log.warn("GetA::"+workflow.getOperators().get("id_1").getOutputs().get("A"));
		
//		Operator id_2 = new Operator();
//		id_2.setId("id_2");
//		id_2.setContent("FOX-MS");
////		id_2.setContent("OPEN-IE-MS");
//		id_2.setType("extractor");
//		id_2.getInputs().put("B", "RDF");
////		workflow.getNextOperators(id_2);
//		workflow.getOperators().put("id_2", id_2);
//		//log.warn("Workflow Object::"+ workflow());
//		
//		log.warn("WF STRING::"+workflow.getLinks().get(0).toString());


//		log.warn("getConnector::"+workflow.getLinks().get(0).getFromConnector());
//		log.warn("ID::"+workflow.getOperators().get("id_2").getId());
//		log.warn("Content::"+workflow.getOperators().get("id_2").getContent());
//		log.warn("GetB::"+workflow.getOperators().get("id_2").getInputs().get("B"));
		
//		String uri = "http://EXECUTER-MS/executeSimple?data=%2FtestData.txt&extractor=OPEN-IE-MS&targetGraph=sask";
		
//		String uri = "http://EXECUTER-MS/executeWorkflow";
//		Boolean response = restTemplate.postForObject(uri, workflow, Boolean.class);
//		System.out.println("Response found=" + response);
		
			responseData.setContent("done");
			fileInfo.addMessage(responseData);	

	return fileInfo;
	}
	
}


//Repo-microservice storeFile() invoked
//: http://localhost:50070/webhdfs/v1/user/DICE/repo/notes%20of%20presntn.txt?op=CREATE&overwrite=true
//: Repo-microservice getHdfsStructure() invoked
//: http://localhost:50070/webhdfs/v1/user/DICE/repo/?op=LISTSTATUS

//{"operators":{"node_hgibcp02st":{"top":20,"left":100,"properties":{"type":"file","id":"node_hgibcp02st","content":"/testData.txt","title":"testData.txt","inputs":{},"outputs":{"output_55uqv2wzcus":{"label":"NL"},"output_y97vayejxre":{"label":"NL"}}}},"node_178rj2s179x":{"top":0,"left":300,"properties":{"type":"extractor","id":"node_178rj2s179x","content":"FOX-MS","title":"FOX","inputs":{"input_tzil25otre":{"label":"NL"},"input_2hfqgk71ukp":{"label":"NL"}},"outputs":{"output_a2b3epd6nd":{"label":"RDF"},"output_09phmd5hdmvi":{"label":"RDF"}}}},"node_hcj9pytiiml":{"top":0,"left":480,"properties":{"type":"db","id":"node_hcj9pytiiml","content":"sask","title":"sask","inputs":{"input_5ezhp221vou":{"label":"RDF"},"input_g4z7qll7d8s":{"label":"RDF"}},"outputs":{}}}},"links":{"0":{"fromOperator":"node_hgibcp02st","fromConnector":"output_55uqv2wzcus","fromSubConnector":0,"toOperator":"node_178rj2s179x","toConnector":"input_tzil25otre","toSubConnector":0},"1":{"fromOperator":"node_178rj2s179x","fromConnector":"output_a2b3epd6nd","fromSubConnector":0,"toOperator":"node_hcj9pytiiml","toConnector":"input_5ezhp221vou","toSubConnector":0}},"operatorTypes":{}}

//{\"operators\":{\"node_hgibcp02st\":{\"top\":20,\"left\":100,\"properties\":{\"type\":\"file\",\"id\":\"node_hgibcp02st\",\"content\":\"\/testData.txt\",\"title\":\"testData.txt\",\"inputs\":{},\"outputs\":{\"output_55uqv2wzcus\":{\"label\":\"NL\"},\"output_y97vayejxre\":{\"label\":\"NL\"}}}},\"node_178rj2s179x\":{\"top\":0,\"left\":300,\"properties\":{\"type\":\"extractor\",\"id\":\"node_178rj2s179x\",\"content\":\"FOX-MS\",\"title\":\"FOX\",\"inputs\":{\"input_tzil25otre\":{\"label\":\"NL\"},\"input_2hfqgk71ukp\":{\"label\":\"NL\"}},\"outputs\":{\"output_a2b3epd6nd\":{\"label\":\"RDF\"},\"output_09phmd5hdmvi\":{\"label\":\"RDF\"}}}},\"node_hcj9pytiiml\":{\"top\":0,\"left\":480,\"properties\":{\"type\":\"db\",\"id\":\"node_hcj9pytiiml\",\"content\":\"sask\",\"title\":\"sask\",\"inputs\":{\"input_5ezhp221vou\":{\"label\":\"RDF\"},\"input_g4z7qll7d8s\":{\"label\":\"RDF\"}},\"outputs\":{}}}},\"links\":{\"0\":{\"fromOperator\":\"node_hgibcp02st\",\"fromConnector\":\"output_55uqv2wzcus\",\"fromSubConnector\":0,\"toOperator\":\"node_178rj2s179x\",\"toConnector\":\"input_tzil25otre\",\"toSubConnector\":0},\"1\":{\"fromOperator\":\"node_178rj2s179x\",\"fromConnector\":\"output_a2b3epd6nd\",\"fromSubConnector\":0,\"toOperator\":\"node_hcj9pytiiml\",\"toConnector\":\"input_5ezhp221vou\",\"toSubConnector\":0}},\"operatorTypes\":{}}\r\n

//HttpHeaders headers = new HttpHeaders();
//headers.setContentType(MediaType.APPLICATION_JSON);
//HttpEntity<Workflow> entity = new HttpEntity<Workflow>(workflow,headers);

//ResponseEntity<String> out = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
//log.warn("Status COde::"+out.getStatusCode());
//log.warn("Before rest call.."+restTemplate.postForObject("http://EXECUTER-MS/executeWorkflow",workflow, Workflow.class ));

//try {
//log.warn("In TRy");
//ResponseEntity<String>  response = restTemplate.postForEntity(uri, workflow, String.class);
//log.warn("RESPONE:"+response.getStatusCode());
//log.warn("CONSTRUCT WORKFLOW RESPONSE::"+response );
//} catch(Exception ex) {
//	throw new NullPointerException("Failed to send input to fred (" + ex.getMessage() + ").");
//}
//if (response.isEmpty()) {

//JSONObject obj = new JSONObject();
//try {
//	obj = new JSONObject("{\"operators\":{\"node_hgibcp02st\":{\"top\":20,\"left\":100,\"properties\":{\"type\":\"file\",\"id\":\"node_hgibcp02st\",\"content\":\"\/testData.txt\",\"title\":\"testData.txt\",\"inputs\":{},\"outputs\":{\"output_55uqv2wzcus\":{\"label\":\"NL\"},\"output_y97vayejxre\":{\"label\":\"NL\"}}}},\"node_178rj2s179x\":{\"top\":0,\"left\":300,\"properties\":{\"type\":\"extractor\",\"id\":\"node_178rj2s179x\",\"content\":\"FOX-MS\",\"title\":\"FOX\",\"inputs\":{\"input_tzil25otre\":{\"label\":\"NL\"},\"input_2hfqgk71ukp\":{\"label\":\"NL\"}},\"outputs\":{\"output_a2b3epd6nd\":{\"label\":\"RDF\"},\"output_09phmd5hdmvi\":{\"label\":\"RDF\"}}}},\"node_hcj9pytiiml\":{\"top\":0,\"left\":480,\"properties\":{\"type\":\"db\",\"id\":\"node_hcj9pytiiml\",\"content\":\"sask\",\"title\":\"sask\",\"inputs\":{\"input_5ezhp221vou\":{\"label\":\"RDF\"},\"input_g4z7qll7d8s\":{\"label\":\"RDF\"}},\"outputs\":{}}}},\"links\":{\"0\":{\"fromOperator\":\"node_hgibcp02st\",\"fromConnector\":\"output_55uqv2wzcus\",\"fromSubConnector\":0,\"toOperator\":\"node_178rj2s179x\",\"toConnector\":\"input_tzil25otre\",\"toSubConnector\":0},\"1\":{\"fromOperator\":\"node_178rj2s179x\",\"fromConnector\":\"output_a2b3epd6nd\",\"fromSubConnector\":0,\"toOperator\":\"node_hcj9pytiiml\",\"toConnector\":\"input_5ezhp221vou\",\"toSubConnector\":0}},\"operatorTypes\":{}}\r\n");
//} catch (JSONException e) {
//	log.warn("IN JSON EXCP");
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//Workflow work = g.fromJson(jsonString, Player.class);		
//
//JSONParser parser = new JSONParser();
//Object object = null;
//try {log.warn("File Reading");
//	object = parser.parse(new FileReader("D:\\gitCloneFolder\\NewAutomaticHandlerCode\\sask\\chatbot\\src\\main\\resources\\JsonTestData.json"));
//} catch (IOException | ParseException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//  
//  //convert Object to JSONObject
////  JSONObject jsonObject = (JSONObject)object;
//log.warn("Casting to wf obj::");
//Workflow obj = (Workflow)object;
//ObjectMapper mapper = new ObjectMapper();
//String jsonInString = mapper.writeValueAsString(workflow);
//log.warn(message);

////HttpHeaders headers = new HttpHeaders();
////headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////HttpEntity<Workflow> request1 = new HttpEntity<Workflow>(workflow, headers);
//
////ResponseEntity<?> response = restTemplate.postForEntity("http://EXECUTER-MS/executeWorkflow",request1 , String.class );


//String response = restTemplate.getForObject("http://EXECUTER-MS/executeWorkflow",  String.class, request1 );
//extract from testData.txt using OpenIE
//String response = restTemplate.exchange(uri, String.class, workflow);

//JSONParser parser = new JSONParser();
//Object object = null;
//try {log.warn("File Reading");
//	object = parser.parse(new FileReader("D:\\gitCloneFolder\\NewAutomaticHandlerCode\\sask\\chatbot\\src\\main\\resources\\JsonTestData.json"));
//} catch (IOException | ParseException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//  
//  //convert Object to JSONObject
////  JSONObject jsonObject = (JSONObject)object;
//log.warn("Casting to wf obj::");
//Workflow obj = (Workflow)object;
//ObjectMapper mapper = new ObjectMapper();
//String jsonInString = mapper.writeValueAsString(workflow);
//log.warn(message);

////HttpHeaders headers = new HttpHeaders();
////headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
////HttpEntity<Workflow> request1 = new HttpEntity<Workflow>(workflow, headers);
//
////ResponseEntity<?> response = restTemplate.postForEntity("http://EXECUTER-MS/executeWorkflow",request1 , String.class );


//String response = restTemplate.getForObject("http://EXECUTER-MS/executeWorkflow",  String.class, request1 );
//extract from testData.txt using OpenIE
//String response = restTemplate.exchange(uri, String.class, workflow);

