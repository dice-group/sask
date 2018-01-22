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
	
	var workflowStack = new WorkflowStack({});
	
	var doingStackOperation = false;
	
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
		this.initDragNDrop();
		
		workflowStack.saveWorkflow(this.getWorkflow());
		this.checkWorkflowStack();
	};
	
	/**
	 * Init drag n drop function. To receive data from the repo.
	 */
	Workspace.prototype.initDragNDrop = function() {
		var self = this;
		var flowchart = this.flowchart;
		this.flowchart.droppable({
			accept: 'li.extractor, li.file, li.db', 
	    	drop: function(ev, ui) {
	    		var link = $(ui.draggable).find('a').attr('href');
	    	   
	    		var type;
	    		if($(ui.draggable).hasClass("extractor")) {
	    			type = "extractor";
	    		} else if($(ui.draggable).hasClass("file")) {
	    			type = "file";
	    		} else if($(ui.draggable).hasClass("db")) {
	    			type = "db";
	    		} else {
	    			logError("Unknown type dropped.");
	    		}
	    		
	    		var offset = $(this).offset();
	            var uiPos = ui.position;
	    		var x = (offset.left - uiPos.left - 160);
	    		var y = (offset.top - uiPos.top - 80);
	    		
	    		console.log('x: ' + x + ' y: ' + y);
	            
	    		var newNode = {
	    				yPosition : y * -1,
	    				xPosition : x * -1,
	    				type : type,
	    				id : link
	    		};
	    		
	    		self.addNode(newNode);
	       }
	    });
	}
	
	/**
	 * Init the workspace.
	 */
	Workspace.prototype.initWorkspace = function() {
		this.$element.append('<div style="height: 92%"></div>');
		
		var self = this;
		
		var onAfterChange = function(changeType) {
			// ignnore save on move 
			if(!doingStackOperation && changeType !== 'operator_moved') {
				workflowStack.saveWorkflow(self.getWorkflow());
				self.checkWorkflowStack();
			}
		};
		
		this.flowchart = this.$element.children().eq(1).flowchart({
			onAfterChange : onAfterChange
		});
	};
	
	/**
	 * Init the toolbar.
	 */
	Workspace.prototype.initToolbar = function() {
		this.$element.append('<div>'
				+ '<button type="button" class="btn btn-default"><a href="#"><span class="glyphicon glyphicon-arrow-left "></span> Undo</a></button>'
				+ '<button type="button" class="btn btn-default">Redo <a href="#"><span class="glyphicon glyphicon-arrow-right"></span></a></button>'
				+ '</div>');
		
		undoButton = this.$element.children().eq(0).children().eq(0);
		redoButton = this.$element.children().eq(0).children().eq(1);
		
		var self = this;
		undoButton.click(function() {
			doingStackOperation = true;
			
			var workflow = workflowStack.getLastWorkflow();
			self.loadWorkflow(workflow);
			
			doingStackOperation = false;
			self.checkWorkflowStack();
		});
		
		redoButton.click(function() {
			doingStackOperation = true;
			var workflow = workflowStack.getNextWorkflow();
			self.loadWorkflow(workflow);
			
			doingStackOperation = false;
			self.checkWorkflowStack();
		});
	};
	
	Workspace.prototype.checkWorkflowStack = function() {
		if(workflowStack.hasNext()) {
			redoButton.removeAttr('disabled');
		} else {
			redoButton.attr('disabled','disabled');
		}
		
		if(workflowStack.hasLast()) {
			undoButton.removeAttr('disabled');
		} else {
			undoButton.attr('disabled','disabled');
		}
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
	Workspace.prototype.loadWorkflow = function(workspace) {
		this.flowchart.flowchart('setData', workspace);
	};
	
	/**
	 * Returns the current workflow
	 */
	Workspace.prototype.getWorkflow = function() {
		return this.flowchart.flowchart('getData');
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