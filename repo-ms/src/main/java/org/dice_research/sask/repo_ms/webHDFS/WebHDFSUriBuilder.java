package org.dice_research.sask.repo_ms.webHDFS;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import org.dice_research.sask.repo_ms.Location;

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
	public static URI getCreateURL(Location location, String path, String fileName) throws URISyntaxException {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path + fileName);
		builder.setParameter(OPERATION, WebHDFSOperation.CREATE.name());
		builder.addParameter(WebHDFSParameter.overwrite.name(), "true");
		return builder.build();
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
	public static URI getOpenURL(Location location, String path) throws URISyntaxException {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.OPEN.name());
		return builder.build();
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
	public static URI getHDFSStructureURI(Location location, String path) throws URISyntaxException {

		path = path.startsWith(FORWARDSLASH) ? path : "/" + path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + path);
		builder.setParameter(OPERATION, WebHDFSOperation.LISTSTATUS.name());
		return builder.build();
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
	public static URI getDeleteURI(Location location, String path) throws URISyntaxException {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.DELETE.name());
		builder.addParameter(WebHDFSParameter.replication.name(), "true");
		return builder.build();
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
	public static URI getMkdirURI(Location location, String path) throws URISyntaxException {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.MKDIRS.name());
		return builder.build();
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
	public static URI getRenameURI(Location location, String from, String to) throws URISyntaxException {

		from = from.startsWith(FORWARDSLASH) ? from.substring(1) : from;
		to = to.startsWith(FORWARDSLASH) ? to.substring(1) : to;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + from);
		builder.setParameter(OPERATION, WebHDFSOperation.RENAME.name());
		builder.setParameter(WebHDFSParameter.destination.name(), "/" + HDFSDIRUSERPATH + "/" + location + "/" + to);
		return builder.build();
	}

}
