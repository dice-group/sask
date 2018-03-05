package org.dice_research.sask.repo_ms;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;

public class WebHDFSUriBuilder {

	private static final String SCHEME = "http";
	private static final String HADOOP_HOSTSERVER = "localhost";
	private static final int HADOOP_NAMENODE_PORT = 50070;

	private static final String PROTOCOL = "webhdfs";
	private static final String VERSION = "v1";
	private static final String FORWARDSLASH = "/";

	private static final String HDFSDIRUSERPATH = "user/DICE";
	private static final String OPERATION = "op";

	public static URI getCreateURL(Location location, String path, String fileName) {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;

		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path + fileName);
		builder.setParameter(OPERATION, WebHDFSOperation.CREATE.name());
		builder.addParameter(WebHDFSParameter.overwrite.name(), "true");

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URI getOpenURL(Location location, String path) {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;

		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.OPEN.name());

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URI getHDFSStructureURI(Location location, String path) {

		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + path);
		builder.setParameter(OPERATION, WebHDFSOperation.LISTSTATUS.name());

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URI getDeleteURI(Location location, String path) {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.DELETE.name());
		builder.addParameter(WebHDFSParameter.replication.name(), "true");

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static URI getMkdirURI(Location location, String path) {

		path = path.startsWith(FORWARDSLASH) ? path.substring(1) : path;
		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + path);
		builder.setParameter(OPERATION, WebHDFSOperation.MKDIRS.name());

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static URI getRenameURI(Location location, String from, String to) {

		from = from.startsWith(FORWARDSLASH) ? from.substring(1) : from;
		to = to.startsWith(FORWARDSLASH) ? to.substring(1) : to;

		URIBuilder builder = new URIBuilder();
		builder.setScheme(SCHEME);
		builder.setHost(HADOOP_HOSTSERVER);
		builder.setPort(HADOOP_NAMENODE_PORT);
		builder.setPath(PROTOCOL + "/" + VERSION + "/" + HDFSDIRUSERPATH + "/" + location + "/" + from);
		builder.setParameter(OPERATION, WebHDFSOperation.RENAME.name());
		builder.setParameter(WebHDFSParameter.destination.name(), "/" + HDFSDIRUSERPATH + "/" + location + "/" + to);

		try {
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
