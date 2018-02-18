package org.dice_research.sask.repo_ms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

	private String hostURL = HADOOP_HOSTSERVER + ":" + HADOOP_NAMENODE_PORT + "/" + protocol + "/" + version;
	private String hdfsDirPath = "/user/DICE";
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

	public String readFile(String path, Location location) {

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
		
		String uri = hostURL + hdfsDirPath + path + opString + readOp;
		this.logger.info(uri);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<byte[]> response = restTemplate.exchange(uri,HttpMethod.GET, entity, byte[].class);
		//this.logger.info(response.getStatusCode()+" "+response.getStatusCodeValue());
		
		
		/*
		//http://localhost:2400/webhdfs/v1/user/DICE/repo/Ablauf.txt?op=OPEN
		if (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
			this.logger.info("Create /tempRepo/ -> " + new File(File.separator + "tempRepo").mkdir());
		}
		
		Files.write(Paths.get(File.separator + "tempRepo"+File.separator+"test.txt"), response.getBody());
		return Files.readAllLines(Paths.get(File.separator + "tempRepo"+File.separator+"test.txt")).toString();*/
		return "";
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

		if (!Files.exists(Paths.get(File.separator + "tempRepo"))) {
			this.logger.info("Create /tempRepo/ -> " + new File(File.separator + "tempRepo").mkdir());
		}

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		String tempFileName = null;
		FileOutputStream fo = null;

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue;
			}

			URI uri = URI.create(hostURL + hdfsDirPath + path + file.getOriginalFilename() + opString + createFileOp
					+ overwrite + overwriteValue);// + blocksizeOp + file.getSize() + replicationOp + replicationValue
													// +buffersizeOp + buffersizeValue;
			this.logger.info(uri);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, null, String.class);
			URI nodeLocation = response.getHeaders().getLocation();
			URI nodeURI = URI.create(HADOOP_HOSTSERVER + ":" + HADOOP_DATANODE_PORT + nodeLocation.getPath() + "?" + nodeLocation.getQuery());

			try {
				map = new LinkedMultiValueMap<>();
				tempFileName = File.separator + "tempRepo" + File.separator + file.getOriginalFilename();
				fo = new FileOutputStream(tempFileName);
				fo.write(file.getBytes());
				map.add("file", new FileSystemResource(tempFileName));

			} catch (IOException e) {
				e.printStackTrace();
				break;
			} finally {
				try {
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			logger.info(nodeURI);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
					map, headers);
			response = restTemplate.exchange(nodeURI, HttpMethod.PUT, requestEntity, String.class);
			this.logger.info(response.getBody());
			this.logger.info(response.getHeaders().toString());

			try {
				this.logger
						.info("Delete file: " + tempFileName + " -> " + Files.deleteIfExists(Paths.get(tempFileName)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (Files.exists(Paths.get(File.separator + "tempRepo"))) {
				this.logger.info("Delete Folder:  " + File.separator + "tempRepo -> "
						+ Files.deleteIfExists(Paths.get(File.separator + "tempRepo")));
			}
		} catch (IOException e) {
			e.printStackTrace();
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

		HdfsFile root = new HdfsFile("", path, Types.DIRECTORY);
		dfs(response.getBody(), hostURL + hdfsDirPath + path, root);
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
		URI uri = URI.create(hostURL + hdfsDirPath + hdfsDirRepoPath + path + opString + makeDirOp);
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
