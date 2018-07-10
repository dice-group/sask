package org.dice_research.sask.taipan_ms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dice_research.sask.config.YAMLConfig;
import org.springframework.http.HttpMethod;
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

	private final RestTemplate restTemplate;
	private final String taipanHostserver;
	private final int taipanPort;

	public TaipanMsService(RestTemplate restTemplate, YAMLConfig config) {
		this.restTemplate = restTemplate;
		this.taipanHostserver = config.getHostserver();
		this.taipanPort = config.getPort();
	}

	/**
	 * To generate SML for a given CSV file
	 * 
	 * @param csvContent
	 * @return SML mapping
	 * @throws IOException
	 */
	public String generateSMLMapping(String csvContent) throws IOException {

		InputStream contentStream = new ByteArrayInputStream(csvContent.getBytes());
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				request.getHeaders().add("X-Subject-Namespace", "http://example.org/");
				IOUtils.copy(contentStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(
				String.class, restTemplate.getMessageConverters());
		String url = "http://" + taipanHostserver + ":" + taipanPort + "/table/api/v1.0/generate_sml_mapping";
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
		return result;
	}

	/**
	 * To identify the Subject columns
	 * 
	 * @param csvContent
	 * @return
	 * @throws IOException
	 */
	public String identifySubjectColumn(String csvContent) throws IOException {

		InputStream contentStream = new ByteArrayInputStream(csvContent.getBytes());
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				IOUtils.copy(contentStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(
				String.class, restTemplate.getMessageConverters());
		String url = "http://" + taipanHostserver + ":" + taipanPort + "/table/api/v1.0/identify_subject_column";
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
		return result;
	}

	/**
	 * To recommend properties
	 * 
	 * @param csvContent
	 * @return
	 * @throws IOException
	 */
	public String propertyRecommender(String csvContent) throws IOException {

		InputStream contentStream = new ByteArrayInputStream(csvContent.getBytes());
		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "text/csv");
				request.getHeaders().add("Charset", "UTF-8");
				IOUtils.copy(contentStream, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(true);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(
				String.class, restTemplate.getMessageConverters());
		String url = "http://" + taipanHostserver + ":" + taipanPort + "/table/api/v1.0/recommend_properties";
		String result = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
		return result;
	}

}
