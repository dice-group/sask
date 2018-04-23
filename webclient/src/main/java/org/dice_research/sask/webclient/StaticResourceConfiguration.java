package org.dice_research.sask.webclient;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/*
		 * Utilizing WebJars in Spring Boot, to import jquery, jquery ui and
		 * bootstrap.
		 * https://spring.io/blog/2014/01/03/utilizing-webjars-in-spring-boot
		 */
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**")
			        .addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}
}