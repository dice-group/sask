/**
 * The IIFE for the repository.
 */
;(function ($, window, document) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'repository';
	
	var _default = {
	};
	
	_default.settings = {
			executeCommand: undefined
	};
	
	_default.options = {};
	
	/**
	 * The structure template.
	 */
	var structureTemplate = [ {
		text : 'Data',
		href : '#data',
		type : 'root'
	}, {
		text : 'Extractors',
		href : '#parent2',
		type : 'root',
		nodes : [ {
			text : '#fox',
			href : '#fox',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		}, {
			text : '#rdffred',
			href : '#rdffred',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		}, {
			text : '#boa',
			href : '#boa',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		} ]
	}, {
		text : 'Database',
		href : '#parent3',
		type : 'root',
		nodes : [ {
			text : '#db',
			href : '#db',
			type : 'db',
			icon : 'glyphicon glyphicon-hdd',
		} ]
	} ];
	
	var Repository = function (element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options: this.options,
			init: $.proxy(this.init, this),
			remove: $.proxy(this.remove, this),
			addData: $.proxy(this.addData, this)
		};
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
		
		this.options = $.extend({}, _default.settings, options);
		var self = this;
		this.treeview = this.$element.treeview({
			data : structureTemplate,
			enableLinks : true
		});
		
		this.initRebuildTreeListener();
		this.initClasses();
		this.initContextMenu();
		this.initDragNDrop();
	};
	
	/**
	 * Init the listener on tree rebuild.
	 */
	Repository.prototype.initRebuildTreeListener = function() {
		// to listen on the buildTree method from the underlying plugin:
		// override the default append method and cause it to trigger a custom append event.
		// Then bind a handler to an element for that event
		var originalAppend = $.fn.append;
	    $.fn.append = function () {
	        return originalAppend.apply(this, arguments).trigger("append");
	    };
	    
	    var self = this;
		this.$element.bind("append", function() { 
			self.initClasses();
			self.initDragNDrop();
		});
	}
	
	/**
	 * Set the css classes of the nodes.
	 */
	Repository.prototype.initClasses = function () {
		var self = this;
		this.$element.find('li').each(function() {
			var nodeId = $(this).attr('data-nodeid');
			var node = self.treeview.treeview('getNode', nodeId);
			
			if(node.type) {
				$(this).addClass(node.type);
			}
		});
	};
	
	/**
	 * Init the drag n drop function.
	 */
	Repository.prototype.initDragNDrop = function () {
		this.$element.find('li.file, li.extractor, li.db').draggable({
			helper: "clone",
			start: function(event, ui) {
				ui.helper.width(this.clientWidth);
			}
		});
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
	 * Add the passed structure to the data node.
	 */
	Repository.prototype.addData = function(structure) {		
		if (typeof structure !== 'object') {
			logError("passed parameter 'structure' is not an object");
		}
		
		structureTemplate[0].nodes = structure.nodes;		
		this.options.data = structureTemplate;
		
		this.init(this.options);
	};
	
	/**
	 * Init the context menu.
	 */
	Repository.prototype.initContextMenu = function() {
		self = this;
		new BootstrapMenu('#' + this.elementId + ' li.db', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.executeCommand('addToWorkspace', 'db', target);
				}
			} ]
		});
		
		new BootstrapMenu('#' + this.elementId + ' li.extractor', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.executeCommand('addToWorkspace', 'extractor', target);
				}
			} ]
		});

		new BootstrapMenu('#' + this.elementId + ' li.file', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.executeCommand('addToWorkspace', 'file', target);
				}
			}, {
				name : 'Rename',
				disabled : true,
				onClick : function(target) {
					self.options.executeCommand('rename', 'file', target);
				}
			}, {
				name : 'Delete',
				disabled : true,
				onClick : function(target) {
					self.options.executeCommand('delete', 'file', target);
				}
			} ]
		});

		new BootstrapMenu('#' + this.elementId + ' li.folder', {
			fetchElementData : this.getLinkFromTarget,
			actions : [ {
				name : 'Rename',
				disabled : true,
				onClick : function(target) {
					self.options.executeCommand('rename', 'folder', target);
				}
			}, {
				name : 'Delete',
				disabled : true,
				onClick : function(target) {
					self.options.executeCommand('delete', 'folder', target);
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