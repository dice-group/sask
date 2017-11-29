/**
 * 
 */
package chatbot.core.handlers.qa;


import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import chatbot.core.handlers.*;
import javax.xml.ws.http.HTTPException;

/**
 * @author Prashanth
 *
 */
public class qa extends Handler {
	//Handle Hawk Service.
	 private static final String URL = "http://localhost:8181/simple-search?query="; //URL of Hawk Service.
	 private HttpClient client;
	 private int timeout = 7000;
	public qa() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).build();
        this.client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}
	private String createHTTPRequest(String question) {
        try {
        		System.out.println("here");
        		String[] strgs = question.split(" "); //Remove and create words instead of passing complete sentences. Follow format specified in gitthub rdocumentation
        		String query="";
        		for (int j=0; j<strgs.length; j++) {
        			query+= strgs[j] + "+";
        		}
        		query=query.substring(0 , query.length()-1);
        		System.out.println(query);
        		//query.remove(query.length()-1, query.length()) //Remove last +sign
        		//query+= "%3F";
        		String URLText= URL+query;
        		System.out.println(URLText);
            HttpPost httpPost = new HttpPost(URLText);
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query", question));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            // Error Scenario
            if(response.getStatusLine().getStatusCode() >= 400) {
                System.out.println("Error In HTTP Request");
                throw new HTTPException(response.getStatusLine().getStatusCode());
            }

            return EntityUtils.toString(response.getEntity());
        }
        catch(Exception e) {
        	//Check if we can create a logger.
        		System.out.println("Exception occured in qa.java");
            throw new InternalError();
        }
        
    }
	
	public String search(String question) throws JsonProcessingException, IOException {
		//String answer="";
		String response = createHTTPRequest(question);
		//String 
		//response = output;
		//Print JSON for now.
		System.out.println("Response Received:");
		System.out.println(response);
		
		ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        String Text= rootNode.path("answer").path("value").toString();
      
		return Text;
		
	}
	

}
