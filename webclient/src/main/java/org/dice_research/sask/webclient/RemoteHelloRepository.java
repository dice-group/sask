package org.dice_research.sask.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class RemoteHelloRepository implements HelloRepository {

	private static final String DISCOVERY_SERVICE_URL = "http://DISCOVERY-MICROSERVICE";
	
	@Autowired
	protected RestTemplate restTemplate;

	protected String serviceUrl;
	
	@Override
	public String getRootstuff() {
		return this.restTemplate.getForObject(DISCOVERY_SERVICE_URL, String.class);
	}
	
	@Override
	public String getHello() {
		return this.restTemplate.getForObject(DISCOVERY_SERVICE_URL + "/hello", String.class);
	}

	@Override
	public Hello getHelloJSON() {
		return this.restTemplate.getForObject(DISCOVERY_SERVICE_URL + "/hellojson", Hello.class);
	}
}
