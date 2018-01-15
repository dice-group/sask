var exTreeviewDefaultData = [ {
	text : 'Data',
	href : '#data',
	type : 'root',
	nodes : [ {
		text : 'one Folder',
		href : '#child1',
		type : 'folder',
		nodes : [ {
			text : 'a file',
			icon : 'glyphicon glyphicon-file',
			href : '#grandchild1',
			type : 'file'
		}, {
			text : 'one more file',
			icon : 'glyphicon glyphicon-file',
			href : '#grandchild2',
			type : 'file'
		} ]
	}, {
		text : 'a file',
		href : '#file',
		type : 'file',
		icon : 'glyphicon glyphicon-file',
	} ]
}, {
	text : 'Extractors',
	href : '#parent2',
	type : 'root',
	nodes : [ {
		text : 'FOX',
		href : '#fox',
		type : 'extractor',
		icon : 'glyphicon glyphicon-cloud',
	}, {
		text : 'RDF Fred',
		href : '#rdffred',
		type : 'extractor',
		icon : 'glyphicon glyphicon-cloud',
	}, {
		text : 'BOA',
		href : '#boa',
		type : 'extractor',
		icon : 'glyphicon glyphicon-cloud',
	} ]
}, {
	text : 'Database',
	href : '#parent3',
	type : 'root',
	nodes : [ {
		text : 'the one and only',
		href : '#db',
		type : 'db',
		icon : 'glyphicon glyphicon-hdd',
	} ]
} ];

function executeCommand(command, target) {
	console.log(command + ": " + target);
}

/**
 * The IIFE for the repository.
 */
;(function ($, window, document) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'repository';
	
	var _default = {};
	
	_default.settings = {};
	
	_default.options = {};
	
	var Repository = function (element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options: this.options,
			init: $.proxy(this.init, this),
			remove: $.proxy(this.remove, this)};
	}
	
	/**
	 * Init.
	 */
	Repository.prototype.init = function (options) {	
		// check dependencies
		if(!jQuery().treeview) {
			logError("'treeview' plugin not initialized.");
			return;
		}
		
		if (typeof BootstrapMenu !== 'function') {
			logError("'BootstrapMenu' plugin not initialized.");
			return;
		}
		
		this.tree = this.$element.treeview({
			data : exTreeviewDefaultData,
			enableLinks : true
		});
		
		this.initContextMenu();
	};
	
	/**
	 * Remove.
	 */
	Repository.prototype.remove = function () {
		this.destroy();
		$.removeData(this, pluginName);
		$('#' + this.styleId).remove();
	};
	
	/**
	 * Extract the link from the context menu target
	 */
	Repository.prototype.getLinkFromTarget = function(target) {
		return $(target).find("a").first().attr('href');
	};
	
	/**
	 * Init the context menu.
	 */
	Repository.prototype.initContextMenu = function() {
		new BootstrapMenu('#' + this.elementId + ' li.db', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					executeCommand('add', target);
				}
			} ]
		});
		
		new BootstrapMenu('#' + this.elementId + ' li.extractor', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					executeCommand('add', target);
				}
			} ]
		});

		new BootstrapMenu('#' + this.elementId + ' li.file', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					executeCommand('add', target);
				}
			}, {
				name : 'Rename',
				onClick : function(target) {
					executeCommand('rename', target);
				}
			}, {
				name : 'Copy',
				onClick : function(target) {
					executeCommand('copy', target);
				}
			}, {
				name : 'Paste',
				onClick : function(target) {
					executeCommand('paste', target);
				}
			}, {
				name : 'Delete',
				onClick : function(target) {
					executeCommand('delete', target);
				}
			} ]
		});

		new BootstrapMenu('#' + this.elementId + ' li.folder', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Rename',
				onClick : function(target) {
					executeCommand('rename', target);
				}
			}, {
				name : 'Copy',
				onClick : function(target) {
					executeCommand('copy', target);
				}
			}, {
				name : 'Paste',
				onClick : function(target) {
					executeCommand('paste', target);
				}
			}, {
				name : 'Delete',
				onClick : function(target) {
					executeCommand('delete', target);
				}
			} ]
		});
	};
	
	/**
	 * logging function
	 */
	var logError = function (message) {
		if (window.console) {
			window.console.error(pluginName + ": " + message);
		}
	};
	
	/**
	 * Add plugin.
	 */
	$.fn[pluginName] = function (options, args) {
		// Prevent against multiple instantiations,
		// handle updates and method calls
		var result;

		this.each(function () {
			var _this = $.data(this, pluginName);
			
			if (typeof options === 'string') {
				if (!_this) {
					logError('Not initialized, can not call method : ' + options);
				}
				else if (!$.isFunction(_this[options]) || options.charAt(0) === '_') {
					logError('No such method : ' + options);
				}
				else {
					if (!(args instanceof Array)) {
						args = [ args ];
					}
					result = _this[options].apply(_this, args);
				}
			}
			else if (typeof options === 'boolean') {
				result = _this;
			}
			else {
				$.data(this, pluginName, new Repository(this, $.extend(true, {}, options)));
			}
		});

		return result || this;
	};
	
})(jQuery, window, document);
0