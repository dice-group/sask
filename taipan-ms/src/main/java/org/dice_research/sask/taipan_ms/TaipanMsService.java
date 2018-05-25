package org.dice_research.sask.taipan_ms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents the executer functions.
 * 
 * @author André Sonntag
 *
 */
public class TaipanMsService {

	/**
	 * The logger.
	 */
	private final Logger logger = Logger.getLogger(TaipanMsService.class.getName());

	/**
	 * The rest template.
	 */
	private final RestTemplate restTemplate;

	public TaipanMsService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String generateSMLMapping() throws IOException {

		File csvFile = new File("C:\\Users\\Andre\\Desktop\\file2.csv");
		InputStream fileStream = new FileInputStream(csvFile);
		
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				request.getHeaders().add("X-Subject-Namespace", "http://example.org/");
				IOUtils.copy(fileStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());
		String url = "http://127.0.0.1:5000/table/api/v1.0/generate_sml_mapping";
		logger.info(url);
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);	
		logger.info(result.toString());
		return "done";
	}
	
	public String identifySubjectColumn() throws IOException {

		File csvFile = new File("C:\\Users\\Andre\\Desktop\\file2.csv");
		InputStream fileStream = new FileInputStream(csvFile);
		
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				IOUtils.copy(fileStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());
		String url = "http://127.0.0.1:5000/table/api/v1.0/identify_subject_column";
		logger.info(url);
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);	
		logger.info(result.toString());
		return "done";
	}
	
	public String propertyRecommender() throws IOException {

		File csvFile = new File("C:\\Users\\Andre\\Desktop\\file2.csv");
		InputStream fileStream = new FileInputStream(csvFile);
		
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				IOUtils.copy(fileStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());
		String url = "http://127.0.0.1:5000/table/api/v1.0/recommend_properties";
		logger.info(url);
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);	
		logger.info(result.toString());
		return "done";
	}

}
