/**
 * 
 */
package chatbot.utils.spellcheck;

import java.util.List;
import org.apache.log4j.Logger;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

/**
 * @author Prashanth
 * Class to perform basic Spell check for commonly made user typing mistakes. For example hellop->hello etc.
 * Use JLanguage tool for handling this.
 *
 */
public class SpellCheck {
	private static Logger log = Logger.getLogger(SpellCheck.class.getName());
	public enum LanguageList {ENGLISH, OTHERS};//DEUTSCH, OTHERS}; --> Future extension?
	private LanguageList lang;
	private JLanguageTool langTool;
	private static final Language langObj= new AmericanEnglish();
	//Currently this function is static. But if we want to support different user different language then we need to remove this property and let it be one-one.
	public SpellCheck(LanguageList alang){
		lang = alang;
		//langObj = new AmericanEnglish(); 
		//Future extension. Won't support now since scope is unclear for such a requirement. 
		//Can be a full fledged requirement since rivescript is also currently not flexible to handle queries in other languages.
		/*switch(lang) {
		case ENGLISH:
			langObj = new AmericanEnglish();
		//case DEUTSCH:
		//	langObj = new GermanyGerman();
		default:
			langObj = new AmericanEnglish();
		}
		*/
		
		langTool = new JLanguageTool(langObj);
	}
	//Currently not required. This would be helpful when we support dynamic language change per user.
	/*public void changeLanguage(LanguageList newLang) {
		lang = newLang;
		langObj = new AmericanEnglish();//TODO: Create Hash Map of Enum vs Language Object and create the right one. Currently Language Info unknown.
		langTool = new JLanguageTool(langObj);
	}*/
	
	public String correctSpelling(String query) {
		 String result = query;
		 try {
			 	List<RuleMatch> matches = langTool.check(query);
	            for (RuleMatch match : matches) {
	                String error = query.substring(match.getFromPos(), match.getToPos());
	                List<String> replacements = match.getSuggestedReplacements();
	                if(replacements.size() > 0) {
	                    result = result.replace(error, match.getSuggestedReplacements().get(0));
	                }
	            }
	            log.debug("Original Query = " + query + " , Suggested replacement=" + result);
	        }
		 	
	        catch(Exception e) {
	            log.error("Error in Spell check");
	            e.printStackTrace();
	        }
		 return result;
	}

}
