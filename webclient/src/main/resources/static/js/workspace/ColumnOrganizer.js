/**
 * JQuery plugin for the column organizer. Organizer the order and size of
 * columns in a row.
 * 
 * @author Kevin Haack
 */
;
(function($, window, document) {

	/* global jQuery, console */
	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = 'columnOrganizer';

	/**
	 * The column start order.
	 */
	var order = [];

	var _default = {};

	_default.settings = {
		columnHeader : "h4",
		columnClass : "sask-column",
		columnClassResizable : "resizable",
		columnClassPrefix : "col-lg-",
		arrowTemplate : "<span class=\"glyphicon glyphicon-chevron-down pull-right\"></span>",
		sizeMaxAttribute : "data-sizeMax",
		sizeMinAttribute : "data-sizeMin"
	};

	var ColumnOrganizer = function(element, options) {
		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this),
			maximizeColumn : $.proxy(this.maximizeColumn, this)
		};
	};

	/**
	 * Init.
	 */
	ColumnOrganizer.prototype.init = function(options) {
		this.options = $.extend({}, _default.settings, options);

		// check dependencies
		if (typeof BootstrapMenu !== 'function') {
			logError("'BootstrapMenu' plugin not initialized.");
			return;
		}

		this.saveOrder();
		this.initArrows();
		this.initContextMenu();
	};

	/**
	 * Save the column order and size.
	 */
	ColumnOrganizer.prototype.saveOrder = function() {
		var self = this;

		this.$element.find("." + this.options.columnClass).each(
				function(index) {
					var c = $(this);
					// save order
					order.push(c);
				});
	};

	/**
	 * Init the menu arrows.
	 */
	ColumnOrganizer.prototype.initArrows = function() {
		var self = this;
		var selector = "#" + this.elementId;
		selector += " ." + this.options.columnClass;
		selector += "." + this.options.columnClassResizable;
		selector += " " + this.options.columnHeader;

		$(selector).each(function(index) {
			var arrow = $(self.options.arrowTemplate);
			arrow.css("cursor", "pointer");
			$(this).append(arrow);
		});
	};

	/**
	 * Get size class
	 */
	ColumnOrganizer.prototype.getSizeClass = function(column) {
		var self = this;
		var sizeClass;

		var classList = column.attr('class').split(/\s+/);
		$.each(classList, function(index, item) {
			if (item.startsWith(self.options.columnClassPrefix)) {
				sizeClass = item;
			}
		});

		return sizeClass;
	};

	/**
	 * Scroll to the of the passed column.
	 */
	ColumnOrganizer.prototype.scrollTo = function(column) {
		$(document).scrollTop(column.offset().top);
	}

	/**
	 * Maximize the passed column
	 */
	ColumnOrganizer.prototype.maximizeColumn = function(column) {
		var currentSizeClass = this.getSizeClass(column);
		column.removeClass(currentSizeClass);
		column.addClass(column.attr(this.options.sizeMaxAttribute));

		this.scrollTo(column);
	};

	/**
	 * Minimize the passed column
	 */
	ColumnOrganizer.prototype.minimizeColumn = function(column) {
		var currentSizeClass = this.getSizeClass(column);
		column.removeClass(currentSizeClass);
		column.addClass(column.attr(this.options.sizeMinAttribute));

		this.scrollTo(column);
	};

	/**
	 * Init the context menu.
	 */
	ColumnOrganizer.prototype.initContextMenu = function() {
		var self = this;
		var selector = "#" + this.elementId;
		selector += " ." + this.options.columnClass;
		selector += "." + this.options.columnClassResizable;
		selector += " " + this.options.columnHeader;
		selector += " span";

		new BootstrapMenu(selector, {
			fetchElementData : function(target) {
				var head = $(target.parent());
				var column = $(head.parent());
				return column;
			},
			menuEvent : "click",
			actions : [ {
				name : "Maximize",
				onClick : function(column) {
					self.maximizeColumn(column);
				}
			}, {
				name : "Minimize",
				onClick : function(column) {
					self.minimizeColumn(column);
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
				$.data(this, pluginName, new ColumnOrganizer(this, $.extend(
						true, {}, options)));
			}
		});

		return result || this;
	};
})(jQuery, window, document);
0