;
(function($, window, document) {

	/* global jQuery, console */
	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = 'toolbar';

	/**
	 * Default settings.
	 */
	var _default = {};

	_default.settings = {
		buttongroupTemplate : '<div class="btn-group" role="group"></div>',
		newButtonTemplate : '<button type="button" class="btn btn-default"><a href="#"><span class="glyphicon glyphicon glyphicon-file"></span> New</a></button>',
		undoButtonTemplate : '<button type="button" class="btn btn-default"><a href="#"><span class="glyphicon glyphicon-arrow-left"></span> Undo</a></button>',
		redoButtonTemplate : '<button type="button" class="btn btn-default"><a href="#">Redo <span class="glyphicon glyphicon-arrow-right"></span></a></button>',
		saveButtonTemplate : '<button type="button" class="btn btn-default"><a href="#"><span class="glyphicon glyphicon-floppy-disk"></span> Save</a></button>',
		executeButtonTemplate : '<button type="button" class="btn btn-primary"><span class="glyphicon glyphicon-play"></span> Execute</button>',
		workflownameFieldTemplate : '<span class="pull-right"></span>',
		onNewButtonClick : undefined,
		onUndoButtonClick : undefined,
		onRedoButtonClick : undefined,
		onSaveButtonClick : undefined,
		onExecuteButtonClick : undefined
	};

	_default.options = {};

	/**
	 * The new button.
	 */
	var newButton = undefined;

	/**
	 * The undo button.
	 */
	var undoButton = undefined;

	/**
	 * The redo button.
	 */
	var redoButton = undefined;

	/**
	 * The save button.
	 */
	var saveButton = undefined;
	
	/**
	 * The execute button.
	 */
	var executeButton = undefined;

	/**
	 * The workflow name.
	 */
	var workflownameField = undefined;

	/**
	 * logging function
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(pluginName + ": " + message);
		}
	};

	var Toolbar = function(element, options) {

		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + "-style";

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this),
			disableRedo : $.proxy(this.disableRedo, this),
			disableUndo : $.proxy(this.disableUndo, this),
			disableSave : $.proxy(this.disableSave, this),
			setWorkflowName : $.proxy(this.setWorkflowName, this)
		};
	};

	/**
	 * Init.
	 */
	Toolbar.prototype.init = function(options) {
		this.options = $.extend({}, _default.settings, options);

		this.initButtons();
		this.initListener();
	};

	/**
	 * Init the buttons.
	 */
	Toolbar.prototype.initButtons = function() {
		// buttons
		newButton = $(this.options.newButtonTemplate);
		undoButton = $(this.options.undoButtonTemplate);
		redoButton = $(this.options.redoButtonTemplate);
		saveButton = $(this.options.saveButtonTemplate);
		executeButton = $(this.options.executeButtonTemplate);
		
		var buttongroup = $(this.options.buttongroupTemplate);
		
		buttongroup.append(newButton);
		buttongroup.append(undoButton);
		buttongroup.append(redoButton);
		buttongroup.append(saveButton);
		buttongroup.append(executeButton);
		
		this.$element.append(buttongroup);
		
		// name
		workflownameField = $(this.options.workflownameFieldTemplate);
		this.$element.append(workflownameField);
	};

	/**
	 * Init the listener.
	 */
	Toolbar.prototype.initListener = function() {
		var self = this;

		// new
		newButton.click(function() {
			if (self.options.onNewButtonClick) {
				self.options.onNewButtonClick();
			}
		});

		// undo
		undoButton.click(function() {
			if (self.options.onUndoButtonClick) {
				self.options.onUndoButtonClick();
			}
		});

		// redo
		redoButton.click(function() {
			if (self.options.onRedoButtonClick) {
				self.options.onRedoButtonClick();
			}
		});

		// save
		saveButton.click(function() {
			if (self.options.onSaveButtonClick) {
				self.options.onSaveButtonClick();
			}
		});
		
		// execute
		executeButton.click(function() {
			if (self.options.onExecuteButtonClick) {
				self.options.onExecuteButtonClick();
			}
		});
	};

	/**
	 * Disable the redo button.
	 */
	Toolbar.prototype.disableRedo = function(disable) {
		if (disable) {
			redoButton.removeAttr('disabled');
		} else {
			redoButton.attr("disabled", "disabled");
		}
	};

	/**
	 * Disable the undo button.
	 */
	Toolbar.prototype.disableUndo = function(disable) {
		if (disable) {
			undoButton.removeAttr("disabled");
		} else {
			undoButton.attr('disabled', 'disabled');
		}
	};

	/**
	 * Disable the save button.
	 */
	Toolbar.prototype.disableSave = function(disable) {
		if (disable) {
			saveButton.removeAttr('disabled');
		} else {
			saveButton.attr('disabled', 'disabled');
		}
	};

	/**
	 * Set workflow name.
	 */
	Toolbar.prototype.setWorkflowName = function(name) {
		workflownameField.text(name);
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
					logError("No such method : " + options);
				} else {
					if (!(args instanceof Array)) {
						args = [ args ];
					}
					result = _this[options].apply(_this, args);
				}
			} else if (typeof options === 'boolean') {
				result = _this;
			} else {
				$.data(this, pluginName, new Toolbar(this, $.extend(true, {},
						options)));
			}
		});

		return result || this;
	};

})(jQuery, window, document);