package org.dice_research.sask.repo_ms;

public class MockService {
	
	private final static MockService INSTANCE = new MockService();
	
	private MockService() {
		
	}
	
	public static MockService getInstance() {
		return INSTANCE;
	}
	
	public boolean storeFile(Object n) {
		return true;
	}
	
	public boolean rename(Object n, Object a) {
		return true;
	}
	
	public boolean move(Object n, Object a) {
		return true;
	}
	
	public String createDirectory(String path) {
		return "";
	}
	
	public String delete(String path) {
		return "";
	}
	
	public String getHdfsStructure() {
		return "{\"suffix\":\"Repo\",\"path\":\"/Repo\",\"type\":\"DIRECTORY\",\"fileList\":[{\"suffix\":\"Test1\",\"path\":\"/Repo/Test1\",\"type\":\"DIRECTORY\",\"fileList\":[{\"suffix\":\"Hallo\",\"path\":\"/Repo/Test1/Hallo\",\"type\":\"DIRECTORY\",\"fileList\":[{\"suffix\":\"kms-acls.xml\",\"path\":\"/Repo/Test1/Hallo/kms-acls.xml\",\"type\":\"FILE\",\"fileList\":[]}]},{\"suffix\":\"core-site.xml\",\"path\":\"/Repo/Test1/core-site.xml\",\"type\":\"FILE\",\"fileList\":[]}]},{\"suffix\":\"Test2\",\"path\":\"/Repo/Test2\",\"type\":\"DIRECTORY\",\"fileList\":[{\"suffix\":\"hdfs-site.xml\",\"path\":\"/Repo/Test2/hdfs-site.xml\",\"type\":\"FILE\",\"fileList\":[]}]}]}";
	}
}
