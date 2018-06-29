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
			if (!(microservice.type in microservices)) {
				microservices[microservice.type] = [];
			}

			microservices[microservice.type].push(microservice);
		});
	};

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(this.settings, options);

		if (!this.settings.dao) {
			logError("dao is not defined.");
			return;
		}
	};

	this.construct(options);

	/**
	 * Is true, if a microservice with the type 'repo' is discovered.
	 */
	this.isRepoDiscovered = function() {
		if (!("repo" in microservices)) {
			return false;
		}
		return microservices.repo.length > 0;
	}
	
	/**
	 * Is true, if a microservice with the type 'executer' is discovered.
	 */
	this.isExecuterDiscovered = function() {
		if (!("executer" in microservices)) {
			return false;
		}
		return microservices.executer.length > 0;
	}

	/**
	 * Return the discovered microservice with the type 'repo'.
	 */
	this.getRepo = function() {
		if (!this.isRepoDiscovered()) {
			logError("no microservice with the type 'repo' discovered.");
			return;
		}

		return microservices["repo"][0];
	}
	
	/**
	 * Return the discovered microservice with the type 'executer'.
	 */
	this.getExecuter = function() {
		if (!this.isExecuterDiscovered()) {
			logError("no microservice with the type 'executer' discovered.");
			return;
		}

		return microservices["executer"][0];
	}

	/**
	 * Returns the discovered microservices.
	 */
	this.getMicroservices = function() {
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

			if (typeof self.settings.onRefreshed !== "undefined") {
				self.settings.onRefreshed();
			}
		}

		var error = function(data) {
			if (typeof self.settings.onError !== "undefined") {
				self.settings.onError(data);
			}
		}

		this.settings.dao.discoverMicroservices(success, error);
	};
};