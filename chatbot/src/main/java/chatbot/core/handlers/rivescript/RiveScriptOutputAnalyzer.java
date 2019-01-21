package chatbot.core.handlers.rivescript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rivescript.RiveScript;

import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

public class RiveScriptOutputAnalyzer {

	private static Logger log = Logger.getLogger(RiveScriptOutputAnalyzer.class.getName());
	private static final String ERRMSG = "error";
	private static String yamlTemplateContents = null;
	private static RiveScript bot = null;

	public RiveScriptOutputAnalyzer() {
		resourceLoader();
	}
	
	private static void resourceLoader() {
        
		ClassLoader cl = RiveScriptOutputAnalyzer.class.getClassLoader(); 
		log.debug("Loading the Rive Script Files");
    try {
    		
    		bot = new RiveScript(); 

        // Create a directory to store the rive files which will be used by RiveScript loader
        File temp = new File("rivefiles/");
        temp.delete();
        temp.mkdir();
        
        Resource[] messageResources = new PathMatchingResourcePatternResolver(cl).getResources("classpath*:rivescript/rivefiles/*.rive");
        for (Resource resource: messageResources){
        	
        		File file = new File(temp + "/" + resource.getFilename() +"/");
        		InputStream inputStream = resource.getInputStream();
        		OutputStream outputStream = new FileOutputStream(file);
        		IOUtils.copy(inputStream, outputStream);
        		outputStream.close();

        }
        bot.loadDirectory(temp);
        bot.sortReplies();
        
        // deleting the temp directory post loading the data
        FileUtils.deleteDirectory(temp);
       
    } catch (Exception e) {
    		log.error("resourceLoader, Exception while loading the resouce files,Stack Trace=" + e.getMessage());
		e.printStackTrace();
    }
    
    
    log.debug("Loading the Template file containing the pre-defined questions");
	try {
		Resource[] messageResources = new PathMatchingResourcePatternResolver(cl).getResources("classpath*:rivescript/properties/template.yml");
		// Since there is only one resource file, it is accessed directly
		InputStream templateFileStream = messageResources[0].getInputStream();
        yamlTemplateContents = new BufferedReader(new InputStreamReader(templateFileStream)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
		log.error("resourceLoader, IO Exception while parsing YAML template,Stack Trace=" + e.getMessage());
		e.printStackTrace();
	}
}

	public ResponseList riveHandler(String query) {

		// Initalize the resources 
		resourceLoader();
		ResponseList responselist = new ResponseList();
		String reply = bot.reply("user", query);
		responselist = handleTextMessage(responselist, reply);
		return responselist;
	}

	private Response createPlainTextResponse(String response) {
		Response obj = new Response();
		obj.setContent(response);		
		obj.setTitle("");
		if(log.isDebugEnabled())
			obj.setClassPredicted("Static Rivescript prediction");
		return obj;
	}

	
	// Custom Function to check if Query is found in Rive Script.
	public static boolean isQueryFound(String query) {
		resourceLoader();
		String reply = bot.reply("user", query);
		if ("NOT FOUND".equals(reply)) {
			return false;
		}
		return true;
	}

	private ResponseList handleTextMessage(ResponseList responselist, String textMessage) {

		String responseHandleText = null;
		
		if (isJSONValid(textMessage))  {
			JsonObject jsonInputText = new JsonObject().getAsJsonObject(textMessage);
			if (jsonInputText.has("type")) {
				String typeValue = jsonInputText.get("name").getAsString();
				log.info("HandleTextMessage:,type of data=" + typeValue);
				responseHandleText = generateResponseFromTemplate(typeValue);
				if (!responseHandleText.equals(ERRMSG)) {
					// TODO Get Output after Initializing.
					// TODO: Update Response List as TYPE_URL and update URL and other details
					// completely based on type
					// TODO: Discuss with team on the format for the template file and how to return
					// the block response block as created in the template
					log.info("Current reponse is of the format = " + responseHandleText);
				}
			}
		} else {
			responseHandleText = responseFromRive(textMessage);
			// Response will be of the form {"comment":"Great! You?"}
			log.debug("Response message from Rive = " + textMessage);
			responselist.setMessageType(MessageType.PLAIN_TEXT);
			responselist.addMessage(createPlainTextResponse(textMessage));
		}
		return responselist;
	}

	private String responseFromRive(String comment) {

		Gson gson = new Gson();
		JsonObject responseJsonObject = new JsonObject();
		String response = null;

		responseJsonObject.add("comment", gson.toJsonTree(comment));
		response = gson.toJson(responseJsonObject);
		return response;
	}

	private String generateResponseFromTemplate(String templateValue) {

		String responseForTemplate = null;
		if (!templateValue.equalsIgnoreCase("help")) {
			responseForTemplate = convertYamlToJsonString(yamlTemplateContents);
		}
		return responseForTemplate;
	}

	private static boolean isJSONValid(String jsonInString) {
		Gson gson = new Gson();
		try {
			if(jsonInString.length() > 0 && jsonInString.split("\\s+").length==1) {
				return false;
			} else{
				gson.fromJson(jsonInString, Object.class);
				return true;
			}
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	private static String convertYamlToJsonString(String yamlFileContents) {

		String responseYaml = null;
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = new Object();
		try {
			obj = yamlReader.readValue(yamlFileContents, Object.class);
			ObjectMapper jsonWriter = new ObjectMapper();
			System.out.println(obj.toString());
			responseYaml = jsonWriter.writeValueAsString(obj);

		} catch (JsonParseException e) {
			log.error("convertYamlToJsonString, Exception in Parsing Json,Stack Trace=" + e.getMessage());
			responseYaml = ERRMSG;
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("convertYamlToJsonString, Exception in Mapping Json,Stack Trace=" + e.getMessage());
			responseYaml = ERRMSG;
			e.printStackTrace();
		} catch (IOException e) {
			log.error(
					"convertYamlToJsonString, IO Exception while parsing YAML template,Stack Trace=" + e.getMessage());
			responseYaml = ERRMSG;
			e.printStackTrace();
		}
		return responseYaml;
	}
}
