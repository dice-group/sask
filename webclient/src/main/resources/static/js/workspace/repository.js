/**
 * The IIFE for the repository.
 */
;(function ($, window, document) {

	/* global jQuery, console */

	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = 'repository';
	
	/**
	 * Data access object.
	 */
	var dao = new DAO({});
	
	/**
	 * Dialogs.
	 */
	var dialogs = new Dialogs({});
	
	var _default = {
	};
	
	_default.settings = {
			onAddToWorkspace: undefined
	};
	
	_default.options = {};
	
	/**
	 * The structure template.
	 */
	var structureTemplate = [ {
		text : 'Data',
		id : '#data',
		type : 'root'
	}, {
		text : 'Extractors',
		id : '#parent2',
		type : 'root',
		nodes : [ {
			text : '#fox',
			id : '#fox',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		}, {
			text : '#rdffred',
			id : '#rdffred',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		}, {
			text : '#boa',
			id : '#boa',
			type : 'extractor',
			icon : 'glyphicon glyphicon-cloud',
		} ]
	}, {
		text : 'Database',
		id : '#parent3',
		type : 'root',
		nodes : [ {
			text : '#db',
			id : '#db',
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
			refresh: $.proxy(this.refresh, this)
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
			data : structureTemplate
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
		// override the default append method and cause it to trigger a custom
		// append event.
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
		var self = this;
		this.$element.find('li.file, li.extractor, li.db').draggable({
			helper: "clone",
			start: function(event, ui) {
				var node = self.getNodeFromTarget(this);
				ui.helper.data('node', node);
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
	Repository.prototype.getNodeFromTarget = function(target) {
		var nodeId = $(target).attr('data-nodeid');
		var node = self.treeview.treeview('getNode', nodeId);
		return node;
	};
	
	/**
	 * Refresh the repo.
	 */
	Repository.prototype.refresh = function() {	
		var self = this;
		var success = function(data) {
			structureTemplate[0].id = data.id;
			structureTemplate[0].nodes = data.nodes;		
			self.options.data = structureTemplate;
			
			self.init(self.options);
		}

		var error = function(data) {
			logError(data);
		}
		
		dao.getRepoStructure(success, error);
	};
	
	/**
	 * Init the context menu.
	 */
	Repository.prototype.initContextMenu = function() {
		self = this;
		
		// data root
		new BootstrapMenu('#' + this.elementId + ' li.root[data-nodeid="0"]', {
			fetchElementData : this.getNodeFromTarget,
			actions : [ {
				name : 'New folder',
				onClick : function(target) {
					openNewFolderDialog(target);
				}
			} ]
		});
		
		// db
		new BootstrapMenu('#' + this.elementId + ' li.db', {
			fetchElementData : this.getNodeFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.onAddToWorkspace(target);
				}
			} ]
		});
		
		// extractor
		new BootstrapMenu('#' + this.elementId + ' li.extractor', {
			fetchElementData : this.getNodeFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.onAddToWorkspace(target);
				}
			} ]
		});

		// file
		new BootstrapMenu('#' + this.elementId + ' li.file', {
			fetchElementData : this.getNodeFromTarget,
			actions : [ {
				name : 'Add to Workspace',
				onClick : function(target) {
					self.options.onAddToWorkspace(target);
				}
			}, {
				name : 'Rename',
				onClick : function(target) {
					openRenameDialog(target);
				}
			}, {
				name : 'Remove',
				onClick : function(target) {
					openRemoveDialog(target);
				}
			} ]
		});

		// folder
		new BootstrapMenu('#' + this.elementId + ' li.folder', {
			fetchElementData : this.getNodeFromTarget,
			actions : [  {
				name : 'New folder',
				onClick : function(target) {
					openNewFolderDialog(target);
				}
			}, {
				name : 'Rename',
				onClick : function(target) {
					openRenameDialog(target);
				}
			}, {
				name : 'Remove',
				onClick : function(target) {
					openRemoveDialog(target);
				}
			}]
		});
	};
	
	/**
	 * Open the rename dialog.
	 */
	var openRenameDialog = function (target) {
		var positiv = function() {
			var target = $(this).find('input[name="target"]')
					.val();
			var name = $(this).find('input[name="name"]').val();

			dao.rename(target, name);
			$(this).dialog('close');
		};

		var negativ = function() {
			$(this).dialog("close");
		};

		dialogs.dialogRename(positiv, negativ, target).dialog(
				'open');
	};
	
	/**
	 * Open the new folder dialog.
	 */
	var openNewFolderDialog = function (target) {
		var positiv = function() {
			var target = $(this).find('input[name="target"]')
					.val();
			var name = $(this).find('input[name="name"]').val();

			dao.newFolder(target, name);
			$(this).dialog('close');
		};

		var negativ = function() {
			$(this).dialog("close");
		};

		dialogs.dialogNewFolder(positiv, negativ, target)
				.dialog('open');
	};
	
	/**
	 * Open the remove dialog.
	 */
	var openRemoveDialog = function(target) {
		var positiv = function() {
			var target = $(this).find('input[name="target"]')
					.val();
			dao.remove(target);
			$(this).dialog("close");
		};

		var negativ = function() {
			$(this).dialog("close");
		};

		dialogs.dialogRemove(positiv, negativ, target).dialog(
				'open');
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