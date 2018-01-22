package org.dice_research.sask.repo_ms;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class HadoopService {

	private static final String hostServer = "http://localhost";
	private static final String port = "8080";
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

	private static enum Type {
		FILE, DIRECTORY
	};

	private String hostURL = hostServer + ":" + port + "/" + protocol + "/" + version;
	private String hdfsDirPath = "/user/DICE";
	private String hdfsDirRepoPath = "/Repo";
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

	public HdfsFile getRepoStructure() {
		String uri = hostURL + hdfsDirPath + hdfsDirRepoPath + opString + listFilesOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);

		HdfsFile root = new HdfsFile("Repo", hdfsDirRepoPath, Types.DIRECTORY);
		dfs(response.getBody(), hostURL + hdfsDirPath + hdfsDirRepoPath, root);
		return root;
	}

	private void dfs(String jsonStr, String path, HdfsFile tree) {
		FileStatuses statuses = jsonToFileStatuses(jsonStr);

		for (FileStatus status : statuses.getFileStatuses()) {
			String uri = path + "/" + status.getPathSuffix();
			HdfsFile node = new HdfsFile(status.getPathSuffix(), tree.getPath()+"/"+status.getPathSuffix(), status.getType());
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

	/**
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=MKDIRS[&permission=<OCTAL>]"\n
	 * create a new Directory
	 * 
	 * @param path
	 *            path+directory name
	 * @return <code>true</code> otherwise <code>false</code>
	 */
	public String createDirectory(String path) {
		String uri = hostURL + hdfsDirPath + path + opString + makeDirOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
		return response.getBody();
	}

	public boolean storeFile(File f) {
		return true;
	}

	public boolean rename(String from, String to) {
		return true;
	}

	public boolean move(String from, String to) {
		return true;
	}

	public String delete(String path) {
		/*
		 * check isFile or isDirectory with status();
		 */

		String uri = hostURL + hdfsDirPath + path + opString + deleteFileOp + andOp + recursiveOp + "";
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
		return response.getBody();
	}

	private String status(String path) {
		String uri = hostURL + hdfsDirPath + path + opString + statusOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
		return response.getBody();
	}

	public void showResponseInformation(ResponseEntity<String> response) {
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getBody());
	}
}
