package org.dice_research.sask.repo_ms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HadoopService {

	private Logger logger = Logger.getLogger(RepoMsController.class.getName());
	private static final String TEMP_FOLDER = "tempRepo";

	private RestTemplate restTemplate = new RestTemplate();

	private static HadoopService instance;

	private HadoopService() {
	};

	public static synchronized HadoopService getInstance() {
		if (HadoopService.instance == null) {
			HadoopService.instance = new HadoopService();
		}
		return HadoopService.instance;
	}

	public String readFile(String path, Location location) {

		URI readFileUri = WebHDFSUriBuilder.getOpenURL(location, path);
		this.logger.info(readFileUri);

		RequestCallback requestCallback = request -> request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));		
		ResponseExtractor<Void> responseExtractor = response -> {
		    Path localPath = Paths.get(File.separator + "tempRepo"+File.separator+"test.txt");
		    Files.copy(response.getBody(), localPath);
		    return null;
		};
		
		restTemplate.execute(readFileUri, HttpMethod.GET, requestCallback, responseExtractor);
		
		InputStreamResource resource = new InputStreamResource(new FileInputStream(File.separator + "tempRepo"+File.separator+"test.txt"));
				
		showFileContent(File.separator + "tempRepo"+File.separator+"test.txt");
		
		return "";
	}

	public boolean storeFilesLocal(String path, List<MultipartFile> files, Location location) {
		createTempFolder();
		LinkedMultiValueMap<String, Object> map = null;
		String tempFileName = null;
		FileOutputStream fo = null;

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue;
			}

			try {
				map = new LinkedMultiValueMap<>();
				tempFileName = File.separator + TEMP_FOLDER + File.separator + file.getOriginalFilename();
				fo = new FileOutputStream(tempFileName);
				fo.write(file.getBytes());
				map.add("file", new FileSystemResource(tempFileName));
			} catch (IOException ex) {
				throw new RuntimeException("Unable to create tempfile '" + tempFileName + "'", ex);
			} finally {
				try {
					if (null != fo) {
						fo.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			createFile(location, path, tempFileName, file.getOriginalFilename(), map);

			try {
				Files.deleteIfExists(Paths.get(tempFileName));
			} catch (IOException ex) {
				throw new RuntimeException("Unable to delete temp file '" + tempFileName + "'", ex);
			}
		}

		return true;
	}

	private void createFile(Location location, String path, String tempFileName, String originalFileName,
			LinkedMultiValueMap<String, Object> map) {

		URI createFileURI = WebHDFSUriBuilder.getCreateURL(location, path, originalFileName);
		ResponseEntity<String> response = restTemplate.exchange(createFileURI, HttpMethod.PUT, null, String.class);
		URI nodeLocation = response.getHeaders().getLocation();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
	    logger.info(requestEntity.getBody().toString());
	    
	    
	    response = restTemplate.exchange(nodeLocation, HttpMethod.PUT, requestEntity, String.class);
	}

	public boolean storeContentInFile(String path, String filename, Location location, String content) {
		createTempFolder();

		String tempFileName = File.separator + TEMP_FOLDER + File.separator + filename;

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(tempFileName));
			out.write(content);
		} catch (IOException ex) {
			throw new RuntimeException("Unable to create tempfile '" + tempFileName + "'", ex);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new FileSystemResource(tempFileName));

		createFile(location, path, tempFileName, filename, map);

		//deleteTempFolder();

		return true;
	}

	/****************************************
	 * DONE
	 *************************************************/

	public HDFSFile getHdfsStructure(Location location) {

		String path = "";
		URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path);
		this.logger.info(hdfsStructureURI);
		ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null, String.class);

		HDFSFile root = new HDFSFile("", path, Types.DIRECTORY);
		dfs(location, response.getBody(), path, root);
		return root;
	}

	public String createDirectory(Location location, String path) {
		URI mkdirURI = WebHDFSUriBuilder.getMkdirURI(location, path);
		this.logger.info(mkdirURI);
		ResponseEntity<String> response = restTemplate.exchange(mkdirURI, HttpMethod.PUT, null, String.class);
		return response.getBody();
	}

	public String rename(String from, String to, Location location) {
		URI renameURI = WebHDFSUriBuilder.getRenameURI(location, from, to);
		this.logger.info(renameURI);
		ResponseEntity<String> response = restTemplate.exchange(renameURI, HttpMethod.PUT, null, String.class);
		return response.getBody();
	}

	public String delete(String path, Location location) {
		URI deleteURI = WebHDFSUriBuilder.getDeleteURI(location, path);
		this.logger.info(deleteURI);
		ResponseEntity<String> response = restTemplate.exchange(deleteURI, HttpMethod.DELETE, null, String.class);
		return response.getBody();
	}

	// https://stackoverflow.com/questions/27895376/deserialize-nested-array-as-arraylist-with-jackson
	private FileStatuses jsonToFileStatuses(String jsonStr) {
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
			HDFSFile node = new HDFSFile(status.getPathSuffix(), tree.getPath() + "/" + status.getPathSuffix(),
					status.getType());
			tree.addFileToList(node);

			if (status.getType() == Types.DIRECTORY) {

				URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location,
						path + "/" + status.getPathSuffix());
				this.logger.info(hdfsStructureURI);
				ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null,
						String.class);
				dfs(location, response.getBody(), uri, node);
			}
		}

	}

	private void deleteTempFolder() {
		File temp = new File(File.separator + "tempRepo");
		if (temp.exists()) {

			String[] entries = temp.list();
			for (String s : entries) {
				File currentFile = new File(temp.getPath(), s);
				currentFile.delete();
			}

			temp.delete();
		}
	}

	private void createTempFolder() {
		if (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
			this.logger.info("Create /tempRepo/ -> " + new File(File.separator + "tempRepo").mkdir());
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
