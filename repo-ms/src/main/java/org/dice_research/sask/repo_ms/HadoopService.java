package org.dice_research.sask.repo_ms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HadoopService {

	private Logger logger = Logger.getLogger(RepoMsController.class.getName());

	private static final String HADOOP_HOSTSERVER = "http://localhost";
	private static final int HADOOP_DATANODE_PORT = 50075;
	private static final int HADOOP_NAMENODE_PORT = 50070;

	private static final String TEMP_FOLDER = "tempRepo";

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
	private static final String overwrite = "&overwrite=";

	private static final String replicationValue = "1";
	private static final String buffersizeOp = "&buffersize=";
	private static final String buffersizeValue = "1024";
	private static final String overwriteValue = "true";

	private String uriScheme = "http";
	private String uriHost = "localhost";
	private int uriPort = 50075;
	private String uriHdfs = protocol + "/" + version;

	private String hostURL = HADOOP_HOSTSERVER + ":" + HADOOP_NAMENODE_PORT + "/" + protocol + "/" + version;
	private String hdfsDirUserPath = "/user/DICE";
	private String hdfsDirRepoPath = "/repo";
	private String hdfsDirWorkspacePath = "/workflow";
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

	public boolean readFile(String path, Location location) {

		URI readFileUri = null;

		try {

			readFileUri = WebHDFSUriBuilder.getOpenURL(location, path);
			this.logger.info(readFileUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(readFileUri, HttpMethod.GET, entity, byte[].class);
		// this.logger.info(response.getStatusCode()+"
		// "+response.getStatusCodeValue());

		/*
		 * //http://localhost:2400/webhdfs/v1/user/DICE/repo/Ablauf.txt?op=OPEN if
		 * (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
		 * this.logger.info("Create /tempRepo/ -> " + new File(File.separator +
		 * "tempRepo").mkdir()); }
		 * 
		 * Files.write(Paths.get(File.separator + "tempRepo"+File.separator+"test.txt"),
		 * response.getBody()); return Files.readAllLines(Paths.get(File.separator +
		 * "tempRepo"+File.separator+"test.txt")).toString();
		 */
		return true;
	}

	/**
	 * Store the passed content in the passed file.
	 * 
	 * @param path
	 * @param filename
	 * @param location
	 * @param content
	 * @return
	 */
	public boolean storeContentInFile(String path, String filename, Location location, String content) {
		if (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
			this.logger.info("Create /tempRepo/ -> " + new File(File.separator + "tempRepo").mkdir());
		}

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

		uploadFile(location, path, tempFileName, filename, map);

		deleteTempFolder();

		return true;
	}

	/**
	 * Upload the passed file to hadoop.
	 * 
	 * @param path
	 * @param tempFileName
	 * @param originalFileName
	 * @param map
	 */
	private void uploadFile(Location location, String path, String tempFileName, String originalFileName, LinkedMultiValueMap<String, Object> map) {
		
		URI createFileURI = WebHDFSUriBuilder.getCreateURL(location, path, originalFileName);
		this.logger.info(createFileURI);
		ResponseEntity<String> response = restTemplate.exchange(createFileURI, HttpMethod.PUT, null, String.class);

		URI nodeLocation = response.getHeaders().getLocation();
		//URI nodeURI = URI.create(HADOOP_HOSTSERVER + ":" + HADOOP_DATANODE_PORT + nodeLocation.getPath() + "?"+ nodeLocation.getQuery());
		this.logger.info(nodeLocation);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);
		response = restTemplate.exchange(nodeLocation, HttpMethod.PUT, requestEntity, String.class);
	}

	/**
	 * Store the passed multipartfiles at the passed path and location.
	 * 
	 * @param path
	 * @param files
	 * @param location
	 * @return
	 */
	public boolean storeFiles(String path, List<MultipartFile> files, Location location) {
		if (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
			this.logger.info("Create /tempRepo/ -> " + new File(File.separator + "tempRepo").mkdir());
		}

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

			uploadFile(location, path, tempFileName, file.getOriginalFilename(), map);

			try {
				Files.deleteIfExists(Paths.get(tempFileName));
			} catch (IOException ex) {
				throw new RuntimeException("Unable to delete temp file '" + tempFileName + "'", ex);
			}
		}

		return true;
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

	public HDFSFile getHdfsStructure(Location location) {
		
		String path = "";
		URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path);
		this.logger.info(hdfsStructureURI);
		ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET, null, String.class);

		HDFSFile root = new HDFSFile("", path, Types.DIRECTORY);
		dfs(location, response.getBody(), path, root);
		return root;
	}

	private void dfs(Location location, String jsonStr, String path, HDFSFile tree) {
		FileStatuses statuses = jsonToFileStatuses(jsonStr);

		for (FileStatus status : statuses.getFileStatuses()) {
			String uri = path + "/" + status.getPathSuffix();
			uri.replaceAll(location.name(), "");
			HDFSFile node = new HDFSFile(status.getPathSuffix(), tree.getPath() + forwardSlash + status.getPathSuffix(),status.getType());
			tree.addFileToList(node);

			if (status.getType() == Types.DIRECTORY) {
				
				URI hdfsStructureURI = WebHDFSUriBuilder.getHDFSStructureURI(location, path + "/" + status.getPathSuffix());
				this.logger.info(hdfsStructureURI);
				ResponseEntity<String> response = restTemplate.exchange(hdfsStructureURI, HttpMethod.GET,null, String.class);
				dfs(location,response.getBody(), uri, node);
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

	public String createDirectory(Location location,String path) {
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

}
