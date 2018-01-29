package org.dice_research.sask.repo_ms;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RepoMsController {
	private Logger logger = Logger.getLogger(RepoMsController.class.getName());
	private HadoopService hadoopService = HadoopService.getInstance();

	@RequestMapping(value = "/storeFile")
	public String storeFiles(String path, MultipartFile[] uploadfiles, Location location) throws IOException {
		this.logger.info("Repo-microservice storeFile() invoked");
		
		// Get file name
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
		return new String(Arrays.asList(uploadfiles).get(0).getBytes());
		//return hadoopService.storeFiles(path,Arrays.asList(uploadfiles),location);
	}

	@RequestMapping(value = "/rename")
	public String rename(String from, String to, Location location) {
		this.logger.info("Repo-microservice rename() invoked");
		return hadoopService.rename(from, to, location);
	}

	@RequestMapping(value = "/getHdfsStructure", produces = MediaType.APPLICATION_JSON_VALUE)
	public HdfsFile getHdfsStructure(Location location) {
		this.logger.info("Repo-microservice getRepoStructure() invoked");
		return hadoopService.getHdfsStructure(location);
	}

	@RequestMapping("/delete")
	public String delete(String path, Location location) {
		this.logger.info("Repo-microservice delete() invoked");
		return hadoopService.delete(path, location);
	}

	@RequestMapping("/createDirectory")
	public String createDirectory(String path) {
		this.logger.info("Repo-microservice createDirectory() invoked");
		return hadoopService.createDirectory(path);
	}

}
