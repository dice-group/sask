var Discoverer = function(options) {
	/**
	 * this.
	 */
	var root = this;
	
	/**
	 * The discovered microservices.
	 */
	var microservices = {};

	/**
	 * Plugin settings.
	 */
	this.settings = {
		dao : undefined,
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
		$.extend(this.settings, options);
		
		if(!this.settings.dao) {
			logError('dao is not defined.');
			return;
		}
	};

	this.construct(options);
	
	this.getmicroservices = function() {
		return microservices;
	}

	/**
	 * Discover the microservices.
	 */
	this.discover = function() {
		var self = this;
		var success = function(data) {
			microservices = {};
			sortMicroservices(data);
			
			if(typeof self.settings.onRefreshed !== "undefined") {
				self.settings.onRefreshed();
			}
		}
		
		var error = function(data) {
			if(typeof self.settings.onError !== "undefined") {
				self.settings.onError(data);
			}
		}
		
		this.settings.dao.discoverMicroservices(success, error);
	}
};