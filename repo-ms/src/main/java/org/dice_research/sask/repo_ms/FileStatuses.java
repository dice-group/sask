package org.dice_research.sask.repo_ms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("FileStatuses")
public class FileStatuses implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<FileStatus> fileStatuses;

	@JsonCreator
	public FileStatuses(@JsonProperty("FileStatus") List<FileStatus> fileStatuses) {
		this.fileStatuses = fileStatuses;
	}

	@JsonProperty("FileStatus")
	public List<FileStatus> getFileStatuses() {
		return fileStatuses;
	}

	public void setFileStatuses(List<FileStatus> fileStatuses) {
		this.fileStatuses = fileStatuses;
	}

}
