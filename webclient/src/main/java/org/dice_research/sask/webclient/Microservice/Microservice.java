package org.dice_research.sask.webclient.Microservice;

import org.springframework.cloud.client.ServiceInstance;

public class Microservice {
	
	private final static String KEY_TYPE = "type";
	
	private final static String UNKNOWN_TYPE = "unknown";
	
	private String host;
	private int port;
	private String serviceId;
	private String type;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static Microservice create(ServiceInstance instance) {
		Microservice m = new Microservice();
		m.setHost(instance.getHost());
		m.setPort(instance.getPort());
		m.setServiceId(instance.getServiceId());

		if(instance.getMetadata().containsKey(KEY_TYPE)) {
			m.setType(instance.getMetadata().get(KEY_TYPE));
		} else {
			m.setType(UNKNOWN_TYPE);
		}
		
		return m;
	}
}
