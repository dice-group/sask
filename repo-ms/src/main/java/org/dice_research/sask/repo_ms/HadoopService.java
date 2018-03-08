package org.dice_research.sask.repo_ms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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

public class HadoopService {

	private Logger logger = Logger.getLogger(RepoMsController.class.getName());
	private RestTemplate restTemplate = new RestTemplate();

	public boolean createFile(Location location, String path, String originalFileName, InputStream fis) {
		
		URI createFileURI = WebHDFSUriBuilder.getCreateURL(location, path, originalFileName);
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
		HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(String.class, restTemplate.getMessageConverters());
		restTemplate.execute(nodeLocation, HttpMethod.PUT, requestCallback, responseExtractor);    
		return true;
	}
		
	public boolean storeContentInFile(Location location, String path, String originalFileName, InputStream fis) {
		return createFile(location, path, originalFileName, fis);
	}
	
	public void readFile(Location location, String path, ServletOutputStream ops) {

		URI readFileUri = WebHDFSUriBuilder.getOpenURL(location, path);
		this.logger.info(readFileUri);
		
		RequestCallback requestCallback = request -> request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));				
		ResponseExtractor<Void> responseExtractor = response -> {
			IOUtils.copy(response.getBody(), ops);	
		    return null;
		};

		restTemplate.execute(readFileUri, HttpMethod.GET, requestCallback, responseExtractor);						
	}
	
	public HDFSFile getHdfsStructure(Location location) {

		String path = "";
		URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path);
		this.logger.info(hdfsStructureURI);
		ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null, String.class);

		HDFSFile root = new HDFSFile("", path, Types.DIRECTORY);
		dfs(location, response.getBody(), path, root);
		return root;
	}
	
	public boolean createDirectory(Location location, String path) {
		URI mkdirURI = WebHDFSUriBuilder.getMkdirURI(location, path);
		this.logger.info(mkdirURI);
		ResponseEntity<String> response = restTemplate.exchange(mkdirURI, HttpMethod.PUT, null, String.class);
		return true;
	}

	public boolean rename(String from, String to, Location location) {
		URI renameURI = WebHDFSUriBuilder.getRenameURI(location, from, to);
		this.logger.info(renameURI);
		ResponseEntity<String> response = restTemplate.exchange(renameURI, HttpMethod.PUT, null, String.class);
		return true;
	}

	public boolean delete(String path, Location location) {
		URI deleteURI = WebHDFSUriBuilder.getDeleteURI(location, path);
		this.logger.info(deleteURI);
		ResponseEntity<String> response = restTemplate.exchange(deleteURI, HttpMethod.DELETE, null, String.class);
		return true;
	}
	
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

	private void dfs(Location location, String jsonStr, String path, HDFSFile tree) {
		FileStatuses statuses = jsonToFileStatuses(jsonStr);

		for (FileStatus status : statuses.getFileStatuses()) {
			String uri = path + "/" + status.getPathSuffix();
			uri.replaceAll(location.name(), "");
			HDFSFile node = new HDFSFile(status.getPathSuffix(), tree.getPath() + "/" + status.getPathSuffix(), status.getType());
			tree.addFileToList(node);

			if (status.getType() == Types.DIRECTORY) {
				if (!path.endsWith("/")) {
					path = path + "/";
				}

				URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path + status.getPathSuffix());
				this.logger.info(hdfsStructureURI);
				ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null, String.class);
				dfs(location, response.getBody(), uri, node);
			}
		}

	}
	
	private void showFileContent(String path) {
		File a = new File(path);
		try {
			List<String> content = Files.readAllLines(Paths.get(a.getPath()));
			for (String line : content) {
				logger.info(line);
			}
			logger.info(content.get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
