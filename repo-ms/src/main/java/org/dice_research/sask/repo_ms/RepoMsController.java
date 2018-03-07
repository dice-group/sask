package org.dice_research.sask.repo_ms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepoMsController {
	private Logger logger = Logger.getLogger(RepoMsController.class.getName());
	private HadoopService hadoopService = new HadoopService();

	@RequestMapping(value = "/storeFile")
	public boolean storeFile(HttpServletRequest request) throws FileUploadException, IOException {
		this.logger.info("Repo-microservice storeFile() invoked");
		// http://www.baeldung.com/spring-apache-file-upload

		try {
			Part filePart = request.getPart("file");
			InputStream fileContent = filePart.getInputStream();
			hadoopService.createFile(Location.valueOf(request.getParameter("location")), request.getParameter("path"),
					Paths.get(filePart.getSubmittedFileName()).getFileName().toString(), fileContent);
		} catch (ServletException e) {

			e.printStackTrace();
		}

		return true;
	}

	@RequestMapping(value = "/storeContentInFile")
	public boolean storeContentInFile(HttpServletRequest request) throws IOException {
		this.logger.info("Repo-microservice storeContentInFile() invoked");

		try {
			Part filePart = request.getPart("file");
			InputStream fileContent = filePart.getInputStream();
			hadoopService.createFile(Location.valueOf(request.getParameter("location")), request.getParameter("path"),
					request.getParameter("filename"), fileContent);
		} catch (ServletException e) {

			e.printStackTrace();
		}
		return true;
	}
	
	@RequestMapping(value = "/readFile")
	public void readFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.logger.info("Repo-microservice readFile() invoked");
		hadoopService.readFile(Location.valueOf(request.getParameter("location")), request.getParameter("path"), response.getOutputStream());
	}
	
	@RequestMapping(value = "/getHdfsStructure", produces = MediaType.APPLICATION_JSON_VALUE)
	public HDFSFile getHdfsStructure(Location location) {
		this.logger.info("Repo-microservice getHdfsStructure() invoked");
		return hadoopService.getHdfsStructure(location);
	}

	@RequestMapping(value = "/rename")
	public boolean rename(String from, String to, Location location) {
		this.logger.info("Repo-microservice rename() invoked");
		return hadoopService.rename(from, to, location);
	}
	
	@RequestMapping("/delete")
	public boolean delete(String path, Location location) {
		this.logger.info("Repo-microservice delete() invoked");
		return hadoopService.delete(path, location);
	}

	@RequestMapping("/createDirectory")
	public boolean createDirectory(String path, Location location) {
		this.logger.info("Repo-microservice createDirectory() invoked");
		return hadoopService.createDirectory(location, path);
	}

}
