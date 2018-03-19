package org.dice_research.sask.repo_ms.webHDFS;

import java.net.URI;
import java.net.URISyntaxException;
import org.dice_research.sask.repo_ms.Location;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class is responsible for generating URLs for Docker/Hadoop REST calls.
 * 
 * @author André Sonntag
 */
public class WebHDFSUriBuilder {

	private static final String SCHEME = "http";
	private static final String HADOOP_HOSTSERVER = "localhost";
	private static final int HADOOP_NAMENODE_PORT = 50070;
	private static final String PROTOCOL = "webhdfs";
	private static final String VERSION = "v1";
	private static final String FORWARDSLASH = "/";
	private static final String HDFSDIRUSERPATH = "user/DICE";
	private static final String OPERATION = "op";

	/**
	 * This method creates a URI to create a new file without file data on the
	 * Hadoop System. <br>
	 * curl -i -X PUT "http://HOST:PORT/webhdfs/v1/PATH?op=CREATE[&overwrite=true]"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @param fileName
	 *            name of the file
	 * @return URI for creating a new file. A request with this URI return a
	 *         redirect to the datanode.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getCreateURL(Location location, String path, String fileName) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null|| fileName == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		path = path.endsWith(FORWARDSLASH) ? path : path.substring(path.length());
		fileName = path.startsWith(FORWARDSLASH) ? fileName.substring(1) : fileName;

		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path + fileName);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(OPERATION, WebHDFSOperation.CREATE.name());
		params.add(WebHDFSParameter.overwrite.name(), "true");
		builder.queryParams(params);
		
		return new URI(builder.toUriString());
	}

	/**
	 * This method creates a URI to read file on the Hadoop System. <br>
	 * curl -i -L "http://HOST:PORT/webhdfs/v1/PATH?op=OPEN"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return URI for reading a file.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getOpenURL(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);		
		builder.queryParam(OPERATION, WebHDFSOperation.OPEN.name());
		
		return new URI(builder.toUriString());
	}

	/**
	 * This method creates a URI to get a list of all files in a folder on the
	 * Hadoop System. <br>
	 * curl -i "http://HOST:PORT/webhdfs/v1/PATH?op=LISTSTATUS"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return URI for listing all files in a folder.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getHDFSStructureURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(FORWARDSLASH) ? path : "/" + path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + path);
		builder.queryParam(OPERATION, WebHDFSOperation.LISTSTATUS.name());
		
		return new URI(builder.toUriString());
	}

	/**
	 * This method creates a URI to delete a file or folder on the Hadoop System.
	 * <br>
	 * curl -i -X DELETE
	 * "http://HOST:PORT/webhdfs/v1/PATH?op=DELETE[&recursive=true]"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return URI for deleting a file or folder.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getDeleteURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(OPERATION, WebHDFSOperation.DELETE.name());
		params.add(WebHDFSParameter.replication.name(), "true");
		builder.queryParams(params);
		
		return new URI(builder.toUriString());
	}

	/**
	 * This method creates a URI to make a folder on the Hadoop System. <br>
	 * curl -i -X PUT "http://HOST:PORT/PATH?op=MKDIRS"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param path
	 *            path in the location folder
	 * @return URI for creating a folder.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getMkdirURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.queryParam(OPERATION, WebHDFSOperation.MKDIRS.name());
		
		return new URI(builder.toUriString());
	}

	/**
	 * This method creates a URI to move and rename a file or folder on the Hadoop
	 * System. <br>
	 * curl -i -X PUT "HOST:PORT/webhdfs/v1/PATH?op=RENAME&destination=PATH"
	 * 
	 * @param location
	 *            folder on the Hadoop System
	 * @param from
	 *            source path in the location folder
	 * @param to
	 *            target path in the location folder
	 * @return URI for renaming or moving a file/folder.
	 * @throws URISyntaxException
	 * 
	 */
	public static URI getRenameURI(Location location, String from, String to) throws URISyntaxException, IllegalArgumentException {

		if(location == null || from == null || to == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		from = from.startsWith(FORWARDSLASH) ? from.substring(1) : from;
		to = to.startsWith(FORWARDSLASH) ? to.substring(1) : to;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(SCHEME);
		builder.host(HADOOP_HOSTSERVER);
		builder.port(HADOOP_NAMENODE_PORT);
		builder.path(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + from);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(OPERATION, WebHDFSOperation.RENAME.name());
		params.add(WebHDFSParameter.destination.name(), "/" + HDFSDIRUSERPATH + "/" + location + "/" + to);
		builder.queryParams(params);
		
		return new URI(builder.toUriString());
	}

}
