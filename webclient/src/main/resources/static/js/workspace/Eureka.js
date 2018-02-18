var Eureka = function(options) {
	/**
	 * this.
	 */
	var root = this;

	/**
	 * Plugin settings.
	 */
	var settings = {
		services : {
			repo : {
				uri : './repo-ms/',
				status : undefined,
				description : undefined
			},
			chatbot : {
				uri : './chatbot/',
				status : undefined,
				description : undefined
			},
			fred : {
				uri : './fred-ms/',
				status : undefined,
				description : undefined
			},
			fox : {
				uri : './fox-ms/',
				status : undefined,
				description : undefined
			},
			spotlight : {
				uri : './spotlight-ms/',
				status : undefined,
				description : undefined
			}
		}
	};

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(settings, options);
	};

	this.construct(options);

	/**
	 * Log error on the console.
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(message);
		}
	};

	/**
	 * Fetch the status of the passed service.
	 */
	var fetchStatus = function(service) {
		var uri = service.uri + "health";
		console.log("fetch " + uri);

		$.ajax({
			type : "POST",
			url : uri,
			success : function(data) {
				console.log(data);
			},
			error : function(data) {
				logError(data);
			}
		});
	}

	/**
	 * Return all services.
	 */
	this.getServices = function() {
		return settings.services;
	};

	/**
	 * Fetch the current status of all services.
	 */
	this.fetchStatuses = function() {
		for (var key in settings.services) {
			fetchStatus(settings.services[key]);
		}
	};
};