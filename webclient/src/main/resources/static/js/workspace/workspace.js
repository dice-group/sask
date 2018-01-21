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
	
	var commandStack = new CommandStack({});
	
	var undoButton = undefined;
	
	var redoButton = undefined;

	var Workspace = function(element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this),
			remove : $.proxy(this.remove, this),
			addNode : $.proxy(this.addNode, this),
			load : $.proxy(this.load, this)
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

		this.initToolbar();
		this.initWorkspace();
		this.initContextMenu();	
	};
	
	Workspace.prototype.initWorkspace = function() {
		this.$element.append('<div style="height: 92%"></div>');
		this.flowchart = this.$element.children().eq(1).flowchart({});
	};
	
	Workspace.prototype.initToolbar = function() {
		this.$element.append('<div>'
				+ '<button type="button" class="btn btn-default"><a href="#"><span class="glyphicon glyphicon-arrow-left "></span> Undo</a></button>'
				+ '<button type="button" class="btn btn-default">Redo <a href="#"><span class="glyphicon glyphicon-arrow-right"></span></a></button>'
				+ '</div>');
		
		undoButton = this.$element.children().eq(0).children().eq(0);
		redoButton = this.$element.children().eq(0).children().eq(1);
		
		undoButton.click(function() {
			commandStack.undoCommand();
		});
		
		redoButton.click(function() {
			commandStack.redoCommand();
		});
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
		
		var command = {};
		var self = this;
		command['do'] = function() {
			self.flowchart.flowchart('createOperator', properties.id, newData);
		};
		command['undo'] = function() {
			self.flowchart.flowchart('deleteOperator', properties.id, newData);
		};
		
		commandStack.doCommand(command);

		
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