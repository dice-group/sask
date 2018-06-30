/**
 * The IIFE for the repository.
 */
;
(function($, window, document) {

	/* global jQuery, console */

	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = "workspace";

	/**
	 * Dialogs.
	 */
	var dialogs = new Dialogs({});

	var _default = {};

	_default.settings = {
		forceFileEnding : true,
		fileEnding : ".wf",
		onWorkflowSaved : undefined,
		dao : undefined
	};

	_default.options = {};

	/**
	 * The wokflowStack for undo and redo.
	 */
	var workflowStack = new WorkflowStack({});

	/**
	 * Indicates whether the current operation is a stack operation.
	 */
	var preventStacking = false;

	/**
	 * The toolbar.
	 */
	var toolbar = undefined;

	/**
	 * The workflow id of the current loaded workflow.
	 */
	var workflowId = undefined;

	var Workspace = function(element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + "-style";

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this),
			addNode : $.proxy(this.addNode, this),
			loadWorkflowFromPath : $.proxy(this.loadWorkflowFromPath, this)
		};
	};

	/**
	 * Init.
	 */
	Workspace.prototype.init = function(options) {
		// check dependencies
		if (!jQuery().flowchart) {
			logError("'flowchart' plugin not initialized.");
			return;
		}

		if (!jQuery().toolbar) {
			logError("'toolbar' plugin not initialized.");
			return;
		}

		if (typeof BootstrapMenu !== "function") {
			logError("'BootstrapMenu' plugin not initialized.");
			return;
		}

		this.options = $.extend({}, _default.settings, options);

		if (!this.options.dao) {
			logError("dao is not defined.");
			return;
		}

		this.initToolbar();
		this.initWorkspace();
		this.initContextMenu();
		this.initDragNDrop();

		workflowStack.saveWorkflow(this.getWorkflow());
		this.syncWorkflowStack();
	};

	/**
	 * Init drag n drop function. To receive data from the repo.
	 */
	Workspace.prototype.initDragNDrop = function() {
		var self = this;
		var flowchart = this.flowchart;
		this.flowchart.droppable({
			accept : "li.extractor, li.file, li.db",
			drop : function(ev, ui) {
				var node = ui.helper.data("node");

				// get position
				var pos = ui.offset;
				var dPos = $(this).offset();

				var x = pos.left - dPos.left;
				var y = pos.top - dPos.top;

				// create node
				var newNode = {
					yPosition : y,
					xPosition : x,
					type : node.type,
					text : node.text,
					id : node.id
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

		// change
		var onAfterChange = function(changeType) {
			if (!preventStacking) {
				var workflow = self.getWorkflow();
				self.balanceConnectors(workflow);

				// load changed
				preventStacking = true;
				self.loadWorkflow(workflow);
				preventStacking = false;

				// save changes
				workflowStack.saveWorkflow(workflow);
				self.syncWorkflowStack();
			}
		};

		// onLinkCreate
		var onLinkCreate = function(linkId, linkData) {
			if (preventStacking) {
				return true;
			}

			var workflow = self.getWorkflow();
			var fromOperator = self.flowchart.flowchart("getOperatorData",
					linkData.fromOperator);
			var toOperator = self.flowchart.flowchart("getOperatorData",
					linkData.toOperator);

			var fromConnector = fromOperator.properties.outputs[linkData.fromConnector];
			var toConnector = toOperator.properties.inputs[linkData.toConnector];

			if (fromConnector.label !== toConnector.label) {
				logError("connector type not match");
				return false;
			}

			if (self.linkExists(workflow, linkData.fromOperator, linkData.toOperator)) {
				logError("link already exists");
				return false;
			}

			return true;
		};

		this.flowchart = this.$element.children().eq(1).flowchart({
			onAfterChange : onAfterChange,
			onLinkCreate : onLinkCreate
		});
	};

	/**
	 * Returns true if there is already a link between this operators.
	 */
	Workspace.prototype.linkExists = function(workflow, from, to) {
		var exists = false;

		for ( var l in workflow.links) {
			var link = workflow.links[l];
			if(link.fromOperator === from && link.toOperator === to) {
				exists = true;
			}
		}

		return exists;
	}

	/**
	 * Balance connectors if necessary to all operators.
	 */
	Workspace.prototype.balanceConnectors = function(workflow) {
		for ( var op in workflow.operators) {
			this.balanceOutputs(workflow, workflow.operators[op]);
			this.balanceInputs(workflow, workflow.operators[op]);
		}
	}

	/**
	 * Balance the output connectors if necessary.
	 */
	Workspace.prototype.balanceOutputs = function(workflow, operator) {
		var connectors = operator.properties.outputs;
		var keys = Object.keys(connectors);
		var currentConnectorCount = keys.length;

		if (currentConnectorCount === 0) {
			return;
		}

		var used = [];
		var unused = [];
		for ( var connector in connectors) {
			var isLinked = false;

			for ( var l in workflow.links) {
				var link = workflow.links[l];
				if (link.fromOperator === operator.properties.id
						&& link.fromConnector === connector) {
					isLinked = true;
				}
			}

			if (isLinked) {
				used.push(connector);
			} else {
				unused.push(connector);
			}
		}

		// add connector if less then one free connector
		if (currentConnectorCount - used.length === 0) {
			connectors[this.createUuid("output_")] = {
				label : connectors[keys[0]].label
			};
		}

		// delete unnecessary connectors
		if (currentConnectorCount - used.length >= 2) {
			delete connectors[unused[unused.length - 1]]
		}
	};

	/**
	 * Balance the input connectors if necessary.
	 */
	Workspace.prototype.balanceInputs = function(workflow, operator) {
		var connectors = operator.properties.inputs;
		var keys = Object.keys(connectors);
		var currentConnectorCount = keys.length;

		if (currentConnectorCount === 0) {
			return;
		}

		var used = [];
		var unused = [];
		for ( var connector in connectors) {
			var isLinked = false;

			for ( var l in workflow.links) {
				var link = workflow.links[l];
				if (link.toOperator === operator.properties.id
						&& link.toConnector === connector) {
					isLinked = true;
				}
			}

			if (isLinked) {
				used.push(connectors[connector]);
			} else {
				unused.push(connector);
			}
		}

		// add connector if less then one free connector
		if (currentConnectorCount - used.length === 0) {
			connectors[this.createUuid("input_")] = {
				label : connectors[keys[0]].label
			};
		}
		
		// delete unnecessary connectors
		if (currentConnectorCount - used.length >= 2) {
			delete connectors[unused[unused.length - 1]]
		}
	};

	/**
	 * Init the toolbar.
	 */
	Workspace.prototype.initToolbar = function() {
		var self = this;

		// new
		var onNewButtonClick = function() {
			preventStacking = true;
			self.clearWorkflow();
			preventStacking = false;

			workflowStack.clear();
			self.syncWorkflowStack();
		}

		// undo
		var onUndoButtonClick = function() {
			preventStacking = true;
			var workflow = workflowStack.getLastWorkflow();
			self.loadWorkflow(workflow);
			preventStacking = false;

			self.syncWorkflowStack();
		}

		// redo
		var onRedoButtonClick = function() {
			preventStacking = true;
			var workflow = workflowStack.getNextWorkflow();
			self.loadWorkflow(workflow);
			preventStacking = false;

			self.syncWorkflowStack();
		}

		// save
		var onSaveButtonClick = function() {
			if (workflowId === undefined) {
				self.openNewWorkflowDialog();
			} else {
				self.saveWorkflow();
			}
		}

		// execute
		var onExecuteButtonClick = function() {
			self.executeWorkflow();
		}

		/*
		 * create
		 */
		toolbar = $('<div></div>');
		this.$element.append(toolbar);
		toolbar.toolbar({
			onNewButtonClick : onNewButtonClick,
			onUndoButtonClick : onUndoButtonClick,
			onRedoButtonClick : onRedoButtonClick,
			onSaveButtonClick : onSaveButtonClick,
			onExecuteButtonClick : onExecuteButtonClick
		});
	};

	/**
	 * Execute the workflow.
	 */
	Workspace.prototype.executeWorkflow = function() {
		var self = this;
		var success = function(data) {
			console.log(data);
		}

		var error = function(data) {
			logError(data);
		}

		var workflow = this.getWorkflow();
		this.options.dao.executeWorkflow(success, error, workflow);
	};

	/**
	 * Save the workflow.
	 */
	Workspace.prototype.saveWorkflow = function() {
		if (workflowId === undefined) {
			logError("workflowId not set.");
			return;
		}

		var self = this;
		var success = function(data) {
			self.options.onWorkflowSaved();
		}

		var error = function(data) {
			logError(data);
		}

		var workflow = this.getWorkflow();
		this.options.dao.saveWorkflow(success, error, workflowId, workflow);

		workflowStack.setSaved();

		this.syncWorkflowStack();
	};

	/**
	 * Sync the workflow stack with the ui buttons.
	 */
	Workspace.prototype.syncWorkflowStack = function() {
		toolbar.toolbar("disableRedo", workflowStack.hasNext());
		toolbar.toolbar("disableUndo", workflowStack.hasLast());
		toolbar.toolbar("disableSave", !workflowStack.isSaved());
	};

	/**
	 * Create a uuid with the passed prefix.
	 */
	Workspace.prototype.createUuid = function(prefix) {
		return prefix + "" + Math.random().toString(36).substr(2, 16);
	};

	Workspace.prototype.nodeAlreadyExist = function(type, id) {
		var exist = false;
		var workflow = this.getWorkflow();

		for ( var o in workflow.operators) {
			var operator = workflow.operators[o];

			if (operator.properties.content === id
					&& operator.properties.type === type) {
				exist = true;
			}
		}

		return exist;
	};

	/**
	 * Add a file to the workspace
	 */
	Workspace.prototype.addNode = function(properties) {
		if (this.nodeAlreadyExist(properties.type, properties.id)) {
			logError("node already exist on workspace");
			return;
		}

		var inputs = {};
		var outputs = {};

		switch (properties.type) {
		case 'file':
			outputs[this.createUuid("output_")] = {
				label : 'NL'
			};
			break;
		case 'extractor':
			inputs[this.createUuid("input_")] = {
				label : 'NL'
			};
			outputs[this.createUuid("output_")] = {
				label : 'RDF'
			};
			break;
		case 'db':
			inputs[this.createUuid("input_")] = {
				label : 'RDF'
			};

			break;
		}

		// create unique node id
		var id = this.createUuid("node_");

		// create data
		var newData = {
			top : properties.yPosition,
			left : properties.xPosition,
			properties : {
				type : properties.type,
				id : id,
				content : properties.id,
				title : properties.text,
				inputs : inputs,
				outputs : outputs
			}
		};

		this.flowchart.flowchart("createOperator", id, newData);
	};

	/**
	 * Open the new workflow dialog.
	 */
	Workspace.prototype.openNewWorkflowDialog = function() {
		var self = this;
		var options = this.options;
		var positiv = function() {
			var name = $(this).find('input[name="name"]').val();

			if (options.forceFileEnding) {
				if (!name.endsWith(options.fileEnding)) {
					name += options.fileEnding;
				}
			}

			self.changeWorkflowName(name);
			self.saveWorkflow();
			$(this).dialog("close");
		};

		var negativ = function() {
			$(this).dialog("close");
		};

		dialogs.dialogNewWorkflow(positiv, negativ).dialog("open");
	};

	/**
	 * Change the displayed workflow name.
	 */
	Workspace.prototype.changeWorkflowName = function(name) {
		workflowId = name;
		toolbar.toolbar("setWorkflowName", name);
	}

	/**
	 * Load the passed workspace
	 */
	Workspace.prototype.loadWorkflow = function(workspace) {
		this.flowchart.flowchart("setData", workspace);
	};

	/**
	 * Clear workspace
	 */
	Workspace.prototype.clearWorkflow = function(workspace) {
		this.changeWorkflowName("");
		workflowId = undefined;
		this.flowchart.flowchart("setData", "");
	};

	/**
	 * Load the workflow from the passed repo path.
	 */
	Workspace.prototype.loadWorkflowFromPath = function(path) {
		var self = this;
		var success = function(data) {
			self.flowchart.flowchart("setData", jQuery.parseJSON(data));

			self.changeWorkflowName(path);

			workflowStack.clear();
			workflowStack.setSaved();
			self.syncWorkflowStack();
		};

		var error = function(data) {
			logError(data);
		};

		this.options.dao.getWorkflow(success, error, path);
	};

	/**
	 * Returns the current workflow
	 */
	Workspace.prototype.getWorkflow = function() {
		return this.flowchart.flowchart("getData");
	};

	/**
	 * Extract the link from the context menu target
	 */
	Workspace.prototype.getNameFromTarget = function(target) {
		return target.attr("id");
	};

	/**
	 * Init the context menu.
	 */
	Workspace.prototype.initContextMenu = function() {
		var self = this;

		var onRemove = function(target) {
			self.flowchart.flowchart("deleteOperator", target);
		};

		new BootstrapMenu('#' + this.elementId + ' div.flowchart-operator', {
			fetchElementData : this.getNameFromTarget,
			actions : [ {
				name : 'Remove',
				onClick : onRemove
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