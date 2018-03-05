var Discoverer = function(options) {
	/**
	 * this.
	 */
	var root = this;
	
	/**
	 * Data access object.
	 */
	var dao = new DAO({});
	
	/**
	 * The discovered microservices.
	 */
	var microservices = {};

	/**
	 * Plugin settings.
	 */
	var settings = {
		onRefreshed : undefined,
		onError : undefined
	};

	/**
	 * Log error on the console.
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(message);
		}
	};
	
	/**
	 * Sort the microservices by the type.
	 */
	var sortMicroservices = function(data) {
		data.forEach(function(microservice) {
			if(!(microservice.type in microservices)) {
				microservices[microservice.type] = [];
			}
			
			microservices[microservice.type].push(microservice);
		});
	}

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(settings, options);
	};

	this.construct(options);
	
	this.getmicroservices = function() {
		return microservices;
	}

	/**
	 * Discover the microservices.
	 */
	this.discover = function() {
		var success = function(data) {
			microservices = {};
			sortMicroservices(data);
			
			if(typeof settings.onRefreshed !== "undefined") {
				settings.onRefreshed();
			}
		}
		
		var error = function(data) {
			if(typeof settings.onError !== "undefined") {
				settings.onError(data);
			}
		}
		
		dao.discoverMicroservices(success, error);
	}
};