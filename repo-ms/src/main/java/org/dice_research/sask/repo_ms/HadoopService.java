package org.dice_research.sask.repo_ms;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HadoopService {

	private Logger logger = Logger.getLogger(RepoMsController.class.getName());

	private static final String hostServer = "http://localhost";
	private static final String port = "2400";
	private static final String protocol = "webhdfs";
	private static final String version = "v1";
	private static final String opString = "?op=";
	private static final String forwardSlash = "/";
	private static final String andOp = "&";

	private static final String listFilesOp = "LISTSTATUS";
	private static final String createFileOp = "CREATE";
	private static final String appendFileOp = "APPEND";
	private static final String deleteFileOp = "DELETE";
	private static final String makeDirOp = "MKDIRS";
	private static final String renameFileOp = "RENAME";
	private static final String readOp = "OPEN";
	private static final String statusOp = "GETFILESTATUS";
	private static final String recursiveOp = "&recursive=";
	private static final String recursiveValue = "true";

	private static final String destinationOp = "&destination=";
	private static final String blocksizeOp = "&blocksize=";
	private static final String replicationOp = "&replication=";
	private static final String replicationValue = "1";
	private static final String buffersizeOp = "&buffersize=";
	private static final String buffersizeValue = "1024";

	private String hostURL = hostServer + ":" + port + "/" + protocol + "/" + version;
	private String hdfsDirPath = "/user/DICE";
	private String hdfsDirRepoPath = "/repo";
	private String hdfsDirWorkspacePath = "/workspace";

	private RestTemplate restTemplate = new RestTemplate();
	private Gson gson = new GsonBuilder().create();

	private static HadoopService instance;

	private HadoopService() {
	};

	public static synchronized HadoopService getInstance() {
		if (HadoopService.instance == null) {
			HadoopService.instance = new HadoopService();
		}
		return HadoopService.instance;
	}

	public String storeFiles(String path, List<MultipartFile> files, Location location) {

		switch (location) {
		case REPO:
			path = hdfsDirRepoPath + path;
			break;
		case WORKFLOW:
			path = hdfsDirWorkspacePath + path;
			break;
		default:
			break;
		}


		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; 
			}

			String uri = hostURL + hdfsDirPath + path + file.getOriginalFilename() + opString + createFileOp + andOp
					+ blocksizeOp + file.getSize() + andOp + replicationOp + replicationValue + andOp + buffersizeOp
					+ buffersizeValue;

			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
			URI nodeURI = response.getHeaders().getLocation();
			this.logger.info(response.getHeaders().getLocation());

			HttpEntity<Byte> temp = new HttpEntity<Byte>(null,null);
			response = restTemplate.exchange(nodeURI, HttpMethod.PUT, null, String.class);

		}

		return "";
	}

	public HdfsFile getHdfsStructure(Location location) {

		String path = "";
		switch (location) {
		case REPO:
			path = hdfsDirRepoPath;
			break;
		case WORKFLOW:
			path = hdfsDirWorkspacePath;
			break;
		default:
			break;
		}

		String uri = hostURL + hdfsDirPath + path + opString + listFilesOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

		HdfsFile root = new HdfsFile("", hdfsDirRepoPath, Types.DIRECTORY);
		dfs(response.getBody(), hostURL + hdfsDirPath + hdfsDirRepoPath, root);
		return root;
	}

	private void dfs(String jsonStr, String path, HdfsFile tree) {
		FileStatuses statuses = jsonToFileStatuses(jsonStr);

		for (FileStatus status : statuses.getFileStatuses()) {
			String uri = path + "/" + status.getPathSuffix();
			HdfsFile node = new HdfsFile(status.getPathSuffix(), tree.getPath() + forwardSlash + status.getPathSuffix(),
					status.getType());
			tree.addFileToList(node);

			if (status.getType() == Types.DIRECTORY) {
				ResponseEntity<String> response = restTemplate.exchange(uri + opString + listFilesOp, HttpMethod.GET,
						null, String.class);
				dfs(response.getBody(), uri, node);
			}
		}

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

	public String createDirectory(String path) {
		path = path.startsWith(forwardSlash) ? path : forwardSlash + path;
		String uri = hostURL + hdfsDirPath + hdfsDirRepoPath + path + opString + makeDirOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
		return response.getBody();
	}

	public String rename(String from, String to, Location location) {
		from = from.startsWith(forwardSlash) ? from : forwardSlash + from;
		to = to.startsWith(forwardSlash) ? to : forwardSlash + to;

		String path = "";
		switch (location) {
		case REPO:
			from = hdfsDirRepoPath + from;
			to = hdfsDirPath + hdfsDirRepoPath + to;
			break;
		case WORKFLOW:
			from = hdfsDirWorkspacePath + from;
			to = hdfsDirPath + hdfsDirWorkspacePath + to;
			break;
		default:
			break;
		}

		String uri = hostURL + hdfsDirPath + from + opString + renameFileOp + andOp + destinationOp + to;
		this.logger.info(uri);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
		return response.getBody();
	}

	public String delete(String path, Location location) {
		path = path.startsWith(forwardSlash) ? path : forwardSlash + path;

		switch (location) {
		case REPO:
			path = hdfsDirRepoPath + path;
			break;
		case WORKFLOW:
			path = hdfsDirWorkspacePath + path;
			break;
		default:
			break;
		}

		String uri = hostURL + hdfsDirPath + path + opString + deleteFileOp + andOp + recursiveOp + recursiveValue;
		this.logger.info(uri);

		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
		return response.getBody();
	}

	public void showResponseInformation(ResponseEntity<String> response) {
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getBody());
	}
}
