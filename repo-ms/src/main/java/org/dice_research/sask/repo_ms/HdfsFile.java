package org.dice_research.sask.repo_ms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HdfsFile implements Serializable{

	private String suffix;
	private String path;
	private Types type;
	private List<HdfsFile> fileList;
	
	public HdfsFile(String suffix, String path, Types type) {
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

	public List<HdfsFile> getFileList() {
		return fileList;
	}
	
	public void addFileToList(HdfsFile file) {
		fileList.add(file);
	}

	@Override
	public String toString() {
		return "HdfsFile [suffix=" + suffix + ", path=" + path + ", type=" + type + ", fileList=" + fileList + "]";
	}
	
	
	
	
}
