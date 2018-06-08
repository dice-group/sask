/**
 * The IIFE for the status.
 */
(function($, window, document) {

	/* global jQuery, console */

	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = "status";

	/**
	 * Contains the type lists.
	 */
	var lists = {};

	var _default = {};

	_default.settings = {
		dao : undefined,
		knownTypes : {
			'extractor' : 'Extractors',
			'executer' : 'Executer',
			'webclient' : 'Webclient',
			'repo' : 'Repository',
			'db' : 'Database'
		},
		templates : {
			typeHeader : '<h2></h2>',
			typeList : '<ul class="list-group"></ul>',
			emptyMessage : '<li class="list-group-item"><p class="text-muted text-center">No microservices of this type registered.</p></li>',
			refreshButton : '<button class="btn btn-primary"><span class="glyphicon glyphicon-refresh"></span> Refresh</button>',
			item : {
				type : '<small class="text-muted pull-right"></small>',
				friendlyname : '<span></span>',
				serviceId : '<small class="text-muted"></small>',
				head : '<h4 class="mb-1"></h4>',
				wrapper : '<div class="d-flex w-100 justify-content-between">',
				port : '<p class="mb-1"></p>',
				host : '<p class="mb-1"></p>',
				item : '<li class="list-group-item"></li>'
			}

		}
	};

	_default.options = {};

	var Status = function(element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);
		var self = this;
		return {
			options : this.options,
			init : $.proxy(this.init, this),
			discover : $.proxy(function() {
				self.discover();
			}, this)
		};
	};

	/**
	 * Init.
	 */
	Status.prototype.init = function(options) {
		// check dependencies
		if (typeof DAO !== "function") {
			logError("'DAO' not initialized.");
			return;
		}

		this.options = $.extend({}, _default.settings, options);

		if (!this.options.dao) {
			logError('dao is not defined.');
			return;
		}

		this.initDiscoverer();
		this.initStructure();
		this.discover();
	};

	/**
	 * Start discover.
	 */
	Status.prototype.discover = function() {
		this.options.dao.getDiscoverer().discover();
	}

	/**
	 * Init the discoverer.
	 */
	Status.prototype.initDiscoverer = function() {
		var self = this;
		var discoverer = this.options.dao.getDiscoverer();
		var settings = discoverer.settings;

		settings.onRefreshed = function() {
			self.onMSRefreshed();
		};

		settings.onError = function() {
			self.onRefreshError();
		};
	}

	/**
	 * Init the structure.
	 */
	Status.prototype.initStructure = function() {
		var self = this;
		for ( var type in this.options.knownTypes) {
			// type headers
			var typeHeader = $(this.options.templates.typeHeader);
			typeHeader.text(this.options.knownTypes[type]);
			this.$element.append(typeHeader);

			// type lists
			var typeList = $(this.options.templates.typeList);
			typeList.append(this.options.templates.emptyMessage);
			this.$element.append(typeList);

			// add to lists
			lists[type] = typeList;
		}

		// refresh button
		var refreshButton = $(this.options.templates.refreshButton);
		refreshButton.click(function() {
			self.options.dao.getDiscoverer().discover();
		});
		this.$element.append(refreshButton);
	}

	/**
	 * Clear the type lists.
	 */
	Status.prototype.clearLists = function() {
		for ( var type in lists) {
			lists[type].empty();
		}
	}

	/**
	 * Will be called, when the ms discovered.
	 */
	Status.prototype.onMSRefreshed = function() {
		this.clearLists();
		var microservices = this.options.dao.getDiscoverer().getMicroservices();

		for ( var type in microservices) {
			for ( var microservice in microservices[type]) {
				this.appendMicroservice(microservices[type][microservice]);
			}
		}

		// add empty messages
		for ( var type in lists) {
			if (lists[type].children().length === 0) {
				lists[type].append(this.options.templates.emptyMessage);
			}
		}
	}

	/**
	 * Append the passed microservice to its list.
	 */
	Status.prototype.appendMicroservice = function(microservice) {
		var type = microservice.type;
		var list = lists[type];

		if (!list) {
			logError("unsupported type for " + microservice.serviceId + ": "
					+ microservice.type);
			return

		}

		var type = $(this.options.templates.item.type);
		var friendlyname = $(this.options.templates.item.friendlyname);
		var serviceId = $(this.options.templates.item.serviceId);
		var head = $(this.options.templates.item.head);
		var wrapper = $(this.options.templates.item.wrapper);
		var port = $(this.options.templates.item.port);
		var host = $(this.options.templates.item.host);
		var item = $(this.options.templates.item.item);

		friendlyname.text(microservice.friendlyname);
		serviceId.text(" (" + microservice.serviceId + ")");
		type.text(microservice.type);
		port.text('Port: ' + microservice.port);
		host.text('Host: ' + microservice.host);

		head.append(friendlyname);
		head.append(serviceId);
		item.append(wrapper);
		wrapper.append(head);
		wrapper.append(port);
		wrapper.append(host);
		head.append(type);

		list.append(item);
	}

	/**
	 * Will be called when there was a discover error.
	 */
	Status.prototype.onRefreshError = function(data) {
		logError(data);
	}

	/**
	 * logging function
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(pluginName + ": " + message);
		}
	};

	/**
	 * Add plugin.
	 */
	$.fn[pluginName] = function(options, args) {
		// Prevent against multiple instantiations,
		// handle updates and method calls
		var result;

		this.each(function() {
			var _this = $.data(this, pluginName);

			if (typeof options === 'string') {
				if (!_this) {
					logError('Not initialized, can not call method : '
							+ options);
				} else if (!$.isFunction(_this[options])
						|| options.charAt(0) === '_') {
					logError('No such method : ' + options);
				} else {
					if (!(args instanceof Array)) {
						args = [ args ];
					}
					result = _this[options].apply(_this, args);
				}
			} else if (typeof options === 'boolean') {
				result = _this;
			} else {
				$.data(this, pluginName, new Status(this, $.extend(true, {},
						options)));
			}
		});

		return result || this;
	};
})(jQuery, window, document);
0