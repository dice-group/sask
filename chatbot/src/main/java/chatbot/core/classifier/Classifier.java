/**
 * 
 */
package chatbot.core.classifier;

import chatbot.core.handlers.rivescript.*;
import chatbot.core.handlers.qa.*;
import chatbot.core.handlers.sessa.*;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.utils.spellcheck.SpellCheck;
import chatbot.utils.spellcheck.SpellCheck.LanguageList;
import chatbot.core.handlers.*;
import chatbot.core.handlers.eliza.ElizaHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Prashanth class to Classify the User Input as QA or KS or Normal
 *         emotion.
 */

public class Classifier {
	private static Logger log = Logger.getLogger(Classifier.class.getName());
	public static final String[] questionTerms = { "what", "why", "how", "when", "where", "who", "which" };
	public static final String[] personalTerms = { "i", "me", "my", "your ", "us", "you", "am", "we", "mine", "our",
			"he", "she", "him", "her", "they", "them", "hi", "hello" };

	public static boolean queryContainsQuestion(String inputStr) {
		return Classifier.hasCommonTerms(inputStr, questionTerms);
	}

	public static boolean queryIsPersonal(String inputStr) {
		return Classifier.hasCommonTerms(inputStr, personalTerms);
	}
	private String handlePreProcessing(String query) {
		//Spell Check
		String result = query;
		SpellCheck spell = new SpellCheck(LanguageList.ENGLISH);
		result = spell.correctSpelling(query);
		log.info("Corrected Query:" + result); 
		return result;
	}
	public Handler classify(IncomingRequest request) {
		try {
			String query = request.getRequestContent().get(0).getText().toLowerCase();
			//Preprocess User Input. Do not expect it to be perfect.
			//Query may contain some basic spelling mistakes which require to be corrected.Currently Language is hardcoded. 
			//It should also come from IncomingRequest class in future since it should ideally depend on browser language so that user queries can be answered efficiently.
			//Check Input. It should not contain bad inputs.
			if(query.isEmpty()) {
				log.warn("Handle Null inputs, Throwing Exception here");
				throw new IllegalArgumentException("Null Input");
			}
			query = handlePreProcessing(query);
			//Set Query here to Request for now?
			request.getRequestContent().get(0).setText(query); //Update Request class.
			query = query.toLowerCase(); //Sometimes spell check returns caps.
			RiveScriptQueryHandler basicText = new RiveScriptQueryHandler();
			// isQueryFound method is now moved to RiveScriptOutputAnalyzer class
			// TODO: Do we need two classes for Rivescripts or can we merge of of them
			boolean flag = RiveScriptOutputAnalyzer.isQueryFound(query);
			log.debug("query is :: "+ query);
			if (flag) {
				log.info("basicText execution");
				return basicText;
			}
			else if (queryIsPersonal(query)) {
				log.info("Eliza execution");
				return new ElizaHandler();
			} else if (queryContainsQuestion(query) || query.contains("?") || query.toLowerCase().startsWith("is ")
					|| query.toLowerCase().startsWith("should")) {
				// call HAWK
				log.info("HAWK!");
				return new QAHandler();

			} else {
				// call to SESSA
				log.info("SESSA!");
				return new SessaHandler();

			}
			//return null;
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;

	}

	public static boolean hasCommonTerms(String inputStr, String[] items) {
		List<String> s1List = Arrays.asList(items);
		List<String> s2List = Arrays.asList(inputStr.split("\\s+"));
		HashSet<String> terms = new HashSet<String>(s1List);
		terms.addAll(s2List);
		return terms.size() < s1List.size() + s2List.size();
	}

}
