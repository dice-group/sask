package KeywordWebService;
/**
 * @author Muzammil Ahmed
 * @since 10-07-2018
 */

import org.sask.chatbot.KeywordSearch.QueryDbpedia;

import java.io.Serializable;
import java.security.Key;

/**
 * This class reads incoming jason object.
 */
public class KeywordReponse implements Serializable{
    private String subject;
    private String property;
    private QueryDbpedia queryDbpedia;
    private String s;
//    private String query;

    public KeywordReponse() {
        this.subject = subject;
        this.property = property;
        queryDbpedia = new QueryDbpedia();

    }

      public String getSCCTwoKeywords(){
        String s = queryDbpedia.getSCC(getSubject(), getProperty());
        return s;

      }

    public String getSubject() {
        return subject;
    }


    public String getProperty() {
        return property;
    }

}
