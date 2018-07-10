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
	
	private final String scheme;
	private final String hadoopHostserver;
	private final int hadoopNamenodePort;
	private final String protocol;
	private final String version;
	private final String forwardslash;
	private final String hdfsdiruserpath;
	private final String operation;

	public WebHDFSUriBuilder(String server, int port) {
		this.scheme = "http";
		hadoopHostserver = server;
		hadoopNamenodePort = port;
		protocol = "webhdfs";
		version = "v1";
		forwardslash = "/";
		hdfsdiruserpath = "user/DICE";
		operation = "op";
	}
	
	
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
	public URI getCreateURL(Location location, String path, String fileName) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null|| fileName == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(forwardslash) ? path.substring(1) : path;
		path = path.endsWith(forwardslash) ? path : path.substring(path.length());
		fileName = path.startsWith(forwardslash) ? fileName.substring(1) : fileName;

		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + "/" + path + fileName);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(operation, WebHDFSOperation.CREATE.name());
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
	public URI getOpenURL(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(forwardslash) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + "/" + path);		
		builder.queryParam(operation, WebHDFSOperation.OPEN.name());
		
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
	public URI getHDFSStructureURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(forwardslash) ? path : "/" + path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + path);
		builder.queryParam(operation, WebHDFSOperation.LISTSTATUS.name());
		
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
	public URI getDeleteURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(forwardslash) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + "/" + path);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(operation, WebHDFSOperation.DELETE.name());
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
	public URI getMkdirURI(Location location, String path) throws URISyntaxException, IllegalArgumentException {
		
		if(location == null || path == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		path = path.startsWith(forwardslash) ? path.substring(1) : path;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + "/" + path);
		builder.queryParam(operation, WebHDFSOperation.MKDIRS.name());
		
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
	public URI getRenameURI(Location location, String from, String to) throws URISyntaxException, IllegalArgumentException {

		if(location == null || from == null || to == null) {
			throw new IllegalArgumentException("Parameter is null");
		}
		
		from = from.startsWith(forwardslash) ? from.substring(1) : from;
		to = to.startsWith(forwardslash) ? to.substring(1) : to;
		
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme);
		builder.host(hadoopHostserver);
		builder.port(hadoopNamenodePort);
		builder.path(protocol + "/" + version + "/" + hdfsdiruserpath + "/" + location + "/" + from);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add(operation, WebHDFSOperation.RENAME.name());
		params.add(WebHDFSParameter.destination.name(), "/" + hdfsdiruserpath + "/" + location + "/" + to);
		builder.queryParams(params);
		
		return new URI(builder.toUriString());
	}

}
