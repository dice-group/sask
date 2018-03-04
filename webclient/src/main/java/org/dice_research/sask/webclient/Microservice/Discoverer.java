package org.dice_research.sask.webclient.Microservice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * An object of this class provides methods to discover Microservices.
 * 
 * @author Kevin Haack
 *
 */
public class Discoverer {

	private DiscoveryClient discoveryClient;

	public Discoverer(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	/**
	 * Discover all registered microservices.
	 * 
	 * @return The registered microservices.
	 */
	public List<Microservice> discover() {
		List<Microservice> microservices = new ArrayList<>();

		for (String serviceId : this.discoveryClient.getServices()) {
			List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);

			for (ServiceInstance instance : instances) {
				microservices.add(Microservice.create(instance));
			}
		}

		return microservices;
	}
}
