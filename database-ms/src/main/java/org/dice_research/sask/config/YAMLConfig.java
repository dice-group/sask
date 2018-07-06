package org.dice_research.sask.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author André Sonntag
 */
@Configuration
@ConfigurationProperties(prefix = "fuseki")
@EnableConfigurationProperties
public class YAMLConfig {

	private String hostserver;
	private int port;

	public String getHostserver() {
		return hostserver;
	}

	public void setHostserver(String hostserver) {
		this.hostserver = hostserver;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
