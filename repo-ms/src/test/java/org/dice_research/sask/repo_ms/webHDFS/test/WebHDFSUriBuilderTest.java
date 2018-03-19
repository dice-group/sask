package org.dice_research.sask.repo_ms.webHDFS.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.dice_research.sask.repo_ms.Location;
import org.dice_research.sask.repo_ms.webHDFS.WebHDFSUriBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class WebHDFSUriBuilderTest {

	Location loc = Location.repo;
	String path = "/";
	String fileName = "Test.txt";
	String strCreateURL = "http://localhost:50070/webhdfs/v1/user/DICE/repo/Test.txt?op=CREATE&overwrite=true";
	String stropenURL = "http://localhost:50070/webhdfs/v1/user/DICE/repo/repo?op=OPEN";
	
	@Test
	public void createURLTest() throws URISyntaxException {
		URI createURL = WebHDFSUriBuilder.getCreateURL(loc, path, fileName);
		assertEquals(strCreateURL,createURL.toString());
	}
	
	@Test
	public void createURLWithForwardSlashTest() throws URISyntaxException {
		URI createURL = WebHDFSUriBuilder.getCreateURL(loc, "/"+path, "/"+fileName);
		assertEquals(strCreateURL,createURL.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullParameterCreateURLTest() throws URISyntaxException {
		WebHDFSUriBuilder.getCreateURL(null, path, fileName);
	}
	
	@Ignore
	@Test
	public void openURLTest() throws IllegalArgumentException, URISyntaxException {
		URI openURL = WebHDFSUriBuilder.getOpenURL(loc, path);
		System.out.println(openURL.toString());
	}
	
	

}
