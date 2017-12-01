package org.dice_research.sask.webclient.Bootstrap;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.ui.Model;
import org.dice_research.sask.webclient.Bootstrap.MockSettings;

/**
 * Bootstrapper to run the application.
 * 
 * @author Kevin Haack
 *
 */
public class Bootstrapper {

	/**
	 * The attribute name for content page.
	 */
	private final static String ATTRIBUTE_NAME_CONTENT_PAGE = "contentPage";
	/**
	 * The attribute name for the page title.
	 */
	private final static String ATTRIBUTE_NAME_PAGE_TITLE = "pageTitle";
	/**
	 * The attribute name for the javascripts.
	 */
	private final static String ATTRIBUTE_NAME_JAVASCRIPTS = "scripts";
	/**
	 * The spring model.
	 */
	private final Model model;
	/**
	 * The servlet request.
	 */
	private final HttpServletRequest request;
	/**
	 * The spring resource loader.
	 */
	private final ResourceLoader resourceLoader;
	/**
	 * The settings.
	 */
	private final MockSettings settings;

	public Bootstrapper(Model model, HttpServletRequest request, ResourceLoader resourceLoader) {
		this.model = model;
		this.request = request;
		this.settings = new MockSettings();
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Run the application.
	 * 
	 * @return The page that can be return by spring.
	 */
	public String run() {
		String pageName = getPagenameFromRequest();
		String pagePath = buildPagePath(pageName);
		List<String> scriptString = getScriptStrings(pageName);

		this.model.addAttribute(ATTRIBUTE_NAME_JAVASCRIPTS, scriptString);
		this.model.addAttribute(ATTRIBUTE_NAME_CONTENT_PAGE, pagePath);
		this.model.addAttribute(ATTRIBUTE_NAME_PAGE_TITLE, "SASK");

		String layoutPage = this.settings.get(MockSettings.KEY_PAGE_LAYOUT);
		return layoutPage;
	}

	/**
	 * Returns all javascript files for the passed page name.
	 * 
	 * @param pageName
	 *            The page name.
	 * @return List of all script files.
	 */
	private List<String> getScriptStrings(String pageName) {
		try {
			String pageScriptFolder = this.settings.get(MockSettings.KEY_PAGE_SCRIPT_FOLDER);
			String pageScriptURI = this.settings.get(MockSettings.KEY_PAGE_SCRIPT_URI);
			
			String resourcePattern = "classpath:" + pageScriptFolder + pageName + "\\" + "*.js";
			Resource[] resources = getResourcePatternResolver().getResources(resourcePattern);
			
			List<String> scripts = new LinkedList<>();
			for (Resource r : resources) {
				StringBuilder b = new StringBuilder();
				
				b.append(pageScriptURI);
				b.append(pageName);
				b.append("/");
				b.append(r.getFilename());
				
				scripts.add(b.toString());
			}

			return scripts;
		} catch (Exception ex) {
			throw new RuntimeException("Unable to get script files.", ex);
		}
	}

	/**
	 * Returns the resource pattern resolver.
	 * 
	 * @return resource pattern resolver.
	 */
	private ResourcePatternResolver getResourcePatternResolver() {
		return ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
	}

	/**
	 * Build the complete page path for the passed page name.
	 * 
	 * @param pageName
	 *            The page name.
	 * @return The page path.
	 */
	private String buildPagePath(String pageName) {
		String pageFolder = this.settings.get(MockSettings.KEY_PAGE_FOLDER);
		String suffix = this.settings.get(MockSettings.KEY_SUFFIX);

		return pageFolder + pageName + "/index" + suffix;
	}

	/**
	 * Extract the page name from the request.
	 * 
	 * @return The page name from the request uri or the default if no page is
	 *         passed.
	 */
	private String getPagenameFromRequest() {
		try {
			URI uri = new URI(this.request.getRequestURL()
			                              .toString());
			String page = uri.getPath()
			                 .replace("/", "");

			String defaultPage = this.settings.get(MockSettings.KEY_DEFAULT_PAGE);

			// if no page is specified, select the default page.
			return page.isEmpty() ? defaultPage : page;
		} catch (Exception ex) {
			throw new RuntimeException("Unable to get page from URI.", ex);
		}
	}
}
