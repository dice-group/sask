package org.dice_research.sask.repo_ms.hdfs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * This class map a FileStatus object from the Hadoop System.
 * 
 * @author André Sonntag
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	private long accessTime;
	private int blockSize;
	private int childrenNum;
	private int fileId;
	private String group;
	private int length;
	private long modificationTime;
	private String owner;
	private String pathSuffix;
	private String permission;
	private int replication;
	private int storagePolicy;
	private Types type;

	public long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(int childrenNum) {
		this.childrenNum = childrenNum;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(long modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPathSuffix() {
		return pathSuffix;
	}

	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getReplication() {
		return replication;
	}

	public void setReplication(int replication) {
		this.replication = replication;
	}

	public int getStoragePolicy() {
		return storagePolicy;
	}

	public void setStoragePolicy(int storagePolicy) {
		this.storagePolicy = storagePolicy;
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "FileStatus [accessTime=" + accessTime + ", blockSize=" + blockSize + ", childrenNum=" + childrenNum
				+ ", fileId=" + fileId + ", group=" + group + ", length=" + length + ", modificationTime="
				+ modificationTime + ", owner=" + owner + ", pathSuffix=" + pathSuffix + ", permission=" + permission
				+ ", replication=" + replication + ", storagePolicy=" + storagePolicy + ", type=" + type + "]";
	}

}
