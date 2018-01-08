package org.dice_research.sask.repo_ms;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepoMsController {
	protected Logger logger = Logger.getLogger(RepoMsController.class.getName());

	@Autowired
	private RepoMsService repoMsService;
	
	@GetMapping(value = "/uploadFile", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadFile() {

	}
	
	@GetMapping(value = "/createFolder", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createFolder() {

	}
	
	@GetMapping(value = "/getRepoStructure", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void getRepoStructure() {

	}
	
	@GetMapping(value = "/rename", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void rename() {

	}
	
	@GetMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void move() {

	}
	
	@GetMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void delete() {

	}
	
	
	
}
