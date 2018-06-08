package org.dice_research.sask.repo_ms.webHDFS.test;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.dice_research.sask.repo_ms.Location;
import org.dice_research.sask.repo_ms.webHDFS.WebHDFSUriBuilder;
import org.junit.Test;
/**
 * 
 * @author Andre
 *
 */
public class WebHDFSUriBuilderTest {

	String pathWF = "/Test.wf";
	String pathFile = "/Test.txt";
	String fileName = "Test.txt";
	String strCreateURL = "http://localhost:50070/webhdfs/v1/user/DICE/repo/Test.txt?op=CREATE&overwrite=true";
	String strOpenURL = "http://localhost:50070/webhdfs/v1/user/DICE/workflow/Test.wf?op=OPEN";
	String strRenameURL = "http://localhost:50070/webhdfs/v1/user/DICE/repo/Test.txt?op=RENAME&destination=/user/DICE/repo/Test01.txt";

	
	@Test
	public void createURLTest() throws URISyntaxException {
		URI createURL = WebHDFSUriBuilder.getCreateURL(Location.repo, pathFile, fileName);
		assertEquals(strCreateURL,createURL.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullParameterCreateURLTest() throws URISyntaxException {
		WebHDFSUriBuilder.getCreateURL(null, pathFile, fileName);
	}
	
	@Test
	public void openURLTest() throws URISyntaxException {
		URI openURL = WebHDFSUriBuilder.getOpenURL(Location.workflow, pathWF);
		assertEquals(strOpenURL,openURL.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullParameterOpenURLTest() throws URISyntaxException {
		WebHDFSUriBuilder.getOpenURL(Location.workflow, null);
	}
	
	@Test
	public void renameURLTest() throws URISyntaxException{
		URI renameURL = WebHDFSUriBuilder.getRenameURI(Location.repo, "/Test.txt", "/Test01.txt");
		assertEquals(strRenameURL,renameURL.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void nullParameterRenameURLTest() throws URISyntaxException{
		WebHDFSUriBuilder.getRenameURI(Location.repo, null, "/Test01.txt");
	}

}
