package chatbot.core.handlers.rivescript;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rivescript.RiveScript;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import chatbot.core.handlers.*;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import chatbot.io.response.ResponseList.MessageType;

import java.lang.String;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

public class RiveScriptOutputAnalyzer {

	private static Logger log = Logger.getLogger(RiveScriptOutputAnalyzer.class.getName());
	private static final String TEMPLATE_FILE = "src/main/resources/rivescript/properties/template.yml";
	private static final String ERRMSG = "error";
	private static String yamlTemplateContents = null;
	private static RiveScript bot = null;

	public RiveScriptOutputAnalyzer() {
		
	}
	
	private static void resourceLoader() {
		
		bot = new RiveScript();
		bot.loadDirectory("src/main/resources/rivescript/rivefiles");
		bot.sortReplies();
		
		try {
			yamlTemplateContents = new String(Files.readAllBytes(Paths.get(TEMPLATE_FILE)));
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

		if (isJSONValid(textMessage)) {
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
			gson.fromJson(jsonInString, Object.class);
			return true;
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
