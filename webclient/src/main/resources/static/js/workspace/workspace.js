var exFlowchartData = {
	operators : {
		operator1 : {
			top : 20,
			left : 20,
			properties : {
				type : 'file',
				id : 'aFile',
				title : 'a File',
				inputs : {},
				outputs : {
					output_1 : {
						label : 'NL',
					}
				}
			}
		},
		operator2 : {
			top : 80,
			left : 300,
			properties : {
				type : 'extractor',
				id : 'fox',
				title : 'FOX',
				inputs : {
					input_1 : {
						label : 'NL',
					}
				},
				outputs : {
					output_1 : {
						label : 'RDF',
					}
				}
			}
		},
		operator3 : {
			top : 80,
			left : 500,
			properties : {
				type : 'db',
				id : 'theDB',
				title : 'Target Graph',
				inputs : {
					input_1 : {
						label : 'RDF',
					}
				}
			}
		}
	},
	links : {
		link_1 : {
			fromOperator : 'operator1',
			fromConnector : 'output_1',
			toOperator : 'operator2',
			toConnector : 'input_1',
		},
		link_2 : {
			fromOperator : 'operator2',
			fromConnector : 'output_1',
			toOperator : 'operator3',
			toConnector : 'input_1',
		}
	}
};

/**
 * The IIFE for the repository.
 */
;(function ($, window, document) {

	/*global jQuery, console*/

	'use strict';

	var pluginName = 'workspace';
	
	var _default = {};
	
	_default.settings = {};
	
	_default.options = {};
	
	var Workspace = function (element, options) {

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
	Workspace.prototype.init = function (options) {	
		// check dependencies
		if(!jQuery().flowchart) {
			logError("'flowchart' plugin not initialized.");
			return;
		}
		
		if (typeof BootstrapMenu !== 'function') {
			logError("'BootstrapMenu' plugin not initialized.");
			return;
		}
		
		this.flowchart = this.$element.flowchart({
			data : exFlowchartData
		});
		
		this.initContextMenu();
	};
	
	/**
	 * remove.
	 */
	Workspace.prototype.remove = function () {
		this.destroy();
		$.removeData(this, pluginName);
		$('#' + this.styleId).remove();
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
				$.data(this, pluginName, new Workspace(this, $.extend(true, {}, options)));
			}
		});

		return result || this;
	};

})(jQuery, window, document);
0