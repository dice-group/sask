package org.dice_research.sask.repo_ms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.servlet.ServletOutputStream;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.dice_research.sask.repo_ms.hdfs.FileStatus;
import org.dice_research.sask.repo_ms.hdfs.FileStatuses;
import org.dice_research.sask.repo_ms.hdfs.HDFSFile;
import org.dice_research.sask.repo_ms.hdfs.Types;
import org.dice_research.sask.repo_ms.webHDFS.WebHDFSUriBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is responsible to interacting with the Hadoop System.
 * 
 * @author André Sonntag
 */
public class HadoopService implements IHadoopService {

	private RestTemplate restTemplate;
	private Logger logger = Logger.getLogger(RepoMsController.class.getName());

	public HadoopService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public boolean createFile(Location location, String path, String originalFileName, InputStream fis) {

		URI createFileURI = null;
		try {
			createFileURI = WebHDFSUriBuilder.getCreateURL(location, path, originalFileName);
			logger.info(createFileURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.exchange(createFileURI, HttpMethod.PUT, null, String.class);
		URI nodeLocation = response.getHeaders().getLocation();

		final RequestCallback requestCallback = new RequestCallback() {
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
				request.getHeaders().add("Content-type", "application/octet-stream");
				IOUtils.copy(fis, request.getBody());
			}
		};

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		restTemplate.setRequestFactory(requestFactory);
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(
				String.class, restTemplate.getMessageConverters());
		restTemplate.execute(nodeLocation, HttpMethod.PUT, requestCallback, responseExtractor);
		return true;
	}

	@Override
	public void readFile(Location location, String path, ServletOutputStream ops) {

		URI readFileUri = null;
		try {
			readFileUri = WebHDFSUriBuilder.getOpenURL(location, path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RequestCallback requestCallback = request -> request.getHeaders()
				.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
		ResponseExtractor<Void> responseExtractor = response -> {
			IOUtils.copy(response.getBody(), ops);
			return null;
		};

		restTemplate.execute(readFileUri, HttpMethod.GET, requestCallback, responseExtractor);
	}

	@Override
	public HDFSFile getHdfsStructure(Location location) {

		String path = "";
		URI hdfsStructureURI = null;
		try {
			hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path);
			logger.info(hdfsStructureURI);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null, String.class);

		HDFSFile root = new HDFSFile("", path, Types.DIRECTORY);
		dfs(location, response.getBody(), path, root);
		return root;
	}

	@Override
	public boolean createDirectory(Location location, String path) {
		URI mkdirURI = null;
		try {
			mkdirURI = WebHDFSUriBuilder.getMkdirURI(location, path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.exchange(mkdirURI, HttpMethod.PUT, null, String.class);
		return true;
	}

	@Override
	public boolean rename(Location location, String from, String to) {
		URI renameURI = null;
		try {
			renameURI = WebHDFSUriBuilder.getRenameURI(location, from, to);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.exchange(renameURI, HttpMethod.PUT, null, String.class);
		return true;
	}

	@Override
	public boolean delete(Location location, String path) {
		URI deleteURI = null;
		try {
			deleteURI = WebHDFSUriBuilder.getDeleteURI(location, path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		ResponseEntity<String> response = restTemplate.exchange(deleteURI, HttpMethod.DELETE, null, String.class);
		return true;
	}

	/**
	 * This method parses the json String to a FileStatuses object.
	 * 
	 * @param jsonStr
	 *            JSON String
	 * @return FileStatuses object
	 */
	private FileStatuses jsonToFileStatuses(String jsonStr) {
		// https://stackoverflow.com/questions/27895376/deserialize-nested-array-as-arraylist-with-jackson

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		try {
			FileStatuses fileStatuses = mapper.readValue(jsonStr, FileStatuses.class);
			return fileStatuses;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method performs a deep-first search on the Hadoop System
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param jsonStr
	 *            JSON string from a folder
	 * @param path
	 *            path to the folder where the dfs is running on.
	 * @param tree
	 *            HDFSFile Object
	 */
	private void dfs(Location location, String jsonStr, String path, HDFSFile tree) {
		FileStatuses statuses = jsonToFileStatuses(jsonStr);

		for (FileStatus status : statuses.getFileStatuses()) {
			String uri = path + "/" + status.getPathSuffix();
			uri.replaceAll(location.name(), "");
			HDFSFile node = new HDFSFile(status.getPathSuffix(), tree.getPath() + "/" + status.getPathSuffix(),
					status.getType());
			tree.addFileToList(node);

			if (status.getType() == Types.DIRECTORY) {
				if (!path.endsWith("/")) {
					path = path + "/";
				}

				URI hdfsStructureURI = null;
				try {
					hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path + status.getPathSuffix());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null,
						String.class);
				dfs(location, response.getBody(), uri, node);
			}
		}

	}

}
