package org.dice_research.sask.repo_ms;

import java.io.File;

import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

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
	private static final String recursiveOp ="&recursive=";
	
	private static enum Type {FILE,DIRECTORY};
	
	private String hostURL = hostServer + ":" + port + "/" + protocol + "/" + version;
	private String hdfsDirPath = "/user/dice/";
	private RestTemplate restTemplate = new RestTemplate();
	private Gson gson = new Gson();	 
	
	private static HadoopService instance;
	private HadoopService() {};

	public static synchronized HadoopService getInstance () {
	    if (HadoopService.instance == null) {
	    	HadoopService.instance = new HadoopService ();
	    }
	    return HadoopService.instance;
	  }
	
	/**
	 * curl -i -X PUT "http://<HOST>:<PORT>/<PATH>?op=MKDIRS[&permission=<OCTAL>]"\n
	 * create a new Directory
	 * @param path path+directory name 
	 * @return <code>true</code> otherwise <code>false</code>
	 */
	public String createDirectory(String path) {
		String uri = hostURL+hdfsDirPath+path+opString+makeDirOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
	    return response.getBody();
	}

	public boolean storeFile(File f) {
		return true;
	}

	public String getRepoStructure() {
		String uri = hostURL+hdfsDirPath+opString+listFilesOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
		return null;
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
		
		String uri = hostURL+hdfsDirPath+path+opString+deleteFileOp+andOp+recursiveOp+"";
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
	    return response.getBody();
	}
	
	private String status(String path) {
		String uri = hostURL+hdfsDirPath+path+opString+statusOp;
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
	    return response.getBody();
	}
	
	public void showResponseInformation(ResponseEntity<String> response) {
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getBody());
	}
}
