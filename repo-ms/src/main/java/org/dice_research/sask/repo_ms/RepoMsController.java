package org.dice_research.sask.repo_ms;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepoMsController {
	protected Logger logger = Logger.getLogger(RepoMsController.class.getName());
	private HadoopService hadoopService = HadoopService.getInstance();

	@GetMapping(value = "/storeFile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean uploadFile() {
		this.logger.info("Repo-microservice storeFile() invoked");
		return hadoopService.storeFile(null);
	}

	@RequestMapping("/createDirectory/{path}")
	public String createFolder(@PathVariable("path") String path) {
		this.logger.info("Repo-microservice createDirectory() invoked");
		return hadoopService.createDirectory(path);
	}

	@GetMapping(value = "/getRepoStructure", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String getRepoStructure() {
		this.logger.info("Repo-microservice getRepoStructure() invoked");
		return hadoopService.getRepoStructure();
	}

	@GetMapping(value = "/rename", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean rename(String from, String to) {
		this.logger.info("Repo-microservice rename() invoked");
		return hadoopService.rename(from, to);
	}
	
	@GetMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean move(String from, String to) {
		this.logger.info("Repo-microservice rename() invoked");
		return hadoopService.move(from, to);
	}

	@RequestMapping("/delete/{path}")
	public String delete(@PathVariable("path") String path) {
		this.logger.info("Repo-microservice delete() invoked");
		return hadoopService.delete(path);
	}

}
