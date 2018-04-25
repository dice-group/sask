package org.dice_research.sask.repo_ms.hdfs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class map the Hadoop System folder structure.
 * 
 * @author André Sonntag
 */
public class HDFSFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String suffix;
	private String path;
	private Types type;
	private List<HDFSFile> fileList;

	public HDFSFile(String suffix, String path, Types type) {
		super();
		this.suffix = suffix;
		this.path = path;
		this.type = type;
		this.fileList = new ArrayList<>();
	}

	public String getSuffix() {
		return suffix;
	}

	public String getPath() {
		return path;
	}

	public Types getType() {
		return type;
	}

	public List<HDFSFile> getFileList() {
		return fileList;
	}

	public void addFileToList(HDFSFile file) {
		fileList.add(file);
	}

	@Override
	public String toString() {
		return "HdfsFile [suffix=" + suffix + ", path=" + path + ", type=" + type + ", fileList=" + fileList + "]";
	}

}
