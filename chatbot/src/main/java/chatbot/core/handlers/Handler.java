/**
 * 
 */
package chatbot.core.handlers;

import java.io.IOException;
import javax.xml.ws.http.HTTPException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.ResponseList;

/**
 * @author Prashanth
 *
 */
abstract public class Handler {
	private int timeout = 7000;

	public ResponseList search(IncomingRequest request) throws JsonProcessingException, IOException {
		return null;
	}

	public String sendHTTPRequest(String query) throws ClientProtocolException, IOException {
		HttpClient client;
		RequestConfig requestConfig = RequestConfig.custom()
		                                           .setSocketTimeout(timeout)
		                                           .build();
		client = HttpClientBuilder.create()
		                          .setDefaultRequestConfig(requestConfig)
		                          .build();
		HttpPost httpPost = new HttpPost(query);
		HttpResponse response = client.execute(httpPost);
		// Error Scenario
		if (response.getStatusLine()
		            .getStatusCode() >= 400) {
			System.out.println("Error In HTTP Request");
			throw new HTTPException(response.getStatusLine()
			                                .getStatusCode());
		}
		return EntityUtils.toString(response.getEntity());
	}

}
