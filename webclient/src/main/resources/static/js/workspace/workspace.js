/**
 * The IIFE for the repository.
 */
;
(function($, window, document) {

	/* global jQuery, console */

	'use strict';

	var pluginName = 'workspace';

	var _default = {};

	_default.settings = {};

	_default.options = {};

	var Workspace = function(element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this),
			remove : $.proxy(this.remove, this),

			addNode : $.proxy(this.addNode, this)
		};
	}

	/**
	 * Init.
	 */
	Workspace.prototype.init = function(options) {
		// check dependencies
		if (!jQuery().flowchart) {
			logError("'flowchart' plugin not initialized.");
			return;
		}

		if (typeof BootstrapMenu !== 'function') {
			logError("'BootstrapMenu' plugin not initialized.");
			return;
		}

		this.options = $.extend({}, _default.settings, options);

		this.flowchart = this.$element.flowchart({});

		this.initContextMenu();
	};

	/**
	 * remove.
	 */
	Workspace.prototype.remove = function() {
		this.destroy();
		$.removeData(this, pluginName);
		$('#' + this.styleId).remove();
	};

	/**
	 * Add a file to the workspace
	 */
	Workspace.prototype.addNode = function(properties) {
		switch (properties.type) {
		case 'file':
			var inputs = {};
			var outputs = {
				output_1 : {
					label : 'NL',
				}
			};
			break;
		case 'extractor':
			var inputs = {
				input_1 : {
					label : 'NL',
				}
			};
			var outputs = {
				output_1 : {
					label : 'RDF',
				}
			};
			break;
		case 'db':
			var inputs = {
				input_1 : {
					label : 'RDF',
				}
			};
			var outputs = {};
			break;
		}

		var newData = {
			top : properties.yPosition,
			left : properties.xPosition,
			properties : {
				type : properties.type,
				id : properties.id,
				title : properties.id,
				inputs : inputs,
				outputs : outputs
			}
		};

		this.flowchart.flowchart('createOperator', properties.id, newData);
	};
	
	/**
	 * Load the passed workspace
	 */
	Workspace.prototype.load = function(workspace) {
		
	};

	/**
	 * Extract the link from the context menu target
	 */
	Workspace.prototype.getNameFromTarget = function(target) {
		return target.attr('id');
	};

	/**
	 * Init the context menu.
	 */
	Workspace.prototype.initContextMenu = function() {
		new BootstrapMenu('#' + this.elementId + ' div.flowchart-operator', {
			fetchElementData : this.getNameFromTarget,
			actions : [ {
				name : 'Remove',
				onClick : function(target) {
					executeCommand('remove', target);
				}
			} ]
		});
	};

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
				$.data(this, pluginName, new Workspace(this, $.extend(true, {},
						options)));
			}
		});

		return result || this;
	};

})(jQuery, window, document);
0