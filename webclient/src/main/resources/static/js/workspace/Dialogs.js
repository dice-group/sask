var Dialogs = function(options) {
	/**
	 * this.
	 */
	var root = this;

	/**
	 * Plugin settings.
	 */
	var settings = {};

	/**
	 * Log error on the console.
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(message);
		}
	};

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(settings, options);
	};

	this.construct(options);

	/**
	 * Dialog templates.
	 */
	var dialogTemplates = {
		remove : '<div title="Delete item">'
				+ '<input type="hidden" name="target" />'
				+ '<p>'
				+ 'This item will be permanently deleted and cannot be recovered. Are you sure?'
				+ '</p>' + '</div>',
		rename : '<div title="Rename">'
				+ '<form>'
				+ '<fieldset>'
				+ '<input type="hidden" name="target" />'
				+ '<label for="name">Name</label>'
				+ '<input type="text" name="name" required="required" class="text ui-widget-content ui-corner-all" />'
				+ '</fieldset>' + '</form>' + '</div>',
		newFolder : '<div title="New folder">'
				+ '<form>'
				+ '<fieldset>'
				+ '<input type="hidden" name="target" />'
				+ '<label for="name">Name</label> <input type="text" name="name" required="required" class="text ui-widget-content ui-corner-all" />'
				+ '</fieldset>' + '</form>' + '</div>',
		newWorkflow : '<div title="New workflow">'
				+ '<form>'
				+ '<fieldset>'
				+ '<label for="name">Name</label> <input type="text" name="name" required="required" class="text ui-widget-content ui-corner-all" />'
				+ '</fieldset>' + '</form>' + '</div>'
	};

	/**
	 * Creates a dialog.
	 */
	var createDialog = function(dialog, buttons) {
		var dialog = $(dialogTemplates[dialog]).dialog({
			autoOpen : false,
			resizable : false,
			height : "auto",
			width : "auto",
			modal : true,
			buttons : buttons
		});

		return dialog;
	};

	/**
	 * Remove dialog.
	 */
	this.dialogRemove = function(positiv, negativ, target) {
		var buttons = {
			"OK" : positiv,
			Cancel : negativ
		};

		var dialog = createDialog('remove', buttons);
		dialog.find('input[name="target"]').val(target.id);

		// positiv on enter
		dialog.submit(function(e) {
			$(this).parent().find("button:eq(1)").trigger("click");
			return false;
		});

		return dialog;
	};

	/**
	 * Rename dialog.
	 */
	this.dialogRename = function(positiv, negativ, target) {
		var buttons = {
			"OK" : positiv,
			Cancel : negativ
		};

		var dialog = createDialog('rename', buttons);
		dialog.find('input[name="name"]').val(target.text);
		dialog.find('input[name="target"]').val(target.id);

		// positiv on enter
		dialog.submit(function(e) {
			$(this).parent().find("button:eq(1)").trigger("click");
			return false;
		});

		return dialog;
	};

	this.dialogNewWorkflow = function(positiv, negativ) {
		var buttons = {
			"OK" : positiv,
			Cancel : negativ
		};

		var dialog = createDialog('newWorkflow', buttons);

		// positiv on enter
		dialog.submit(function(e) {
			$(this).parent().find("button:eq(1)").trigger("click");
			return false;
		});

		return dialog;
	};

	/**
	 * New folder dialog.
	 */
	this.dialogNewFolder = function(positiv, negativ, target) {
		var buttons = {
			"OK" : positiv,
			Cancel : negativ
		};

		var dialog = createDialog('newFolder', buttons);
		dialog.find('input[name="target"]').val(target.id);

		// positiv on enter
		dialog.submit(function(e) {
			$(this).parent().find("button:eq(1)").trigger("click");
			return false;
		});

		return dialog;
	};

	/**
	 * Upload dialog.
	 */
	this.dialogUpload = function(positiv, negativ) {
		var buttons = {
			"Upload" : positiv,
			Cancel : negativ
		};

		var dialog = createDialog('upload', buttons);

		// positiv on enter
		dialog.submit(function(e) {
			$(this).parent().find("button:eq(1)").trigger("click");
			return false;
		});

		return dialog;
	};
};