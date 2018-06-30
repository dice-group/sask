var Upload = function(options) {
	/**
	 * this.
	 */
	var root = this;

	/**
	 * Plugin settings.
	 */
	var settings = {
		onUploaded : undefined,
		dao : undefined
	};

	/**
	 * The dialog.
	 */
	var dialog = undefined;

	/**
	 * The form.
	 */
	var form = undefined;

	/**
	 * The Input.
	 */
	var inputFile = undefined;

	/**
	 * The select button.
	 */
	var selectButton = undefined;

	/**
	 * The clear button.
	 */
	var clearButton = undefined;

	/**
	 * The list of uploaded/uploading files.
	 */
	var uploadList = undefined;

	/**
	 * The select file message.
	 */
	var selectFileMessage = undefined;

	/**
	 * The currently uploading files.
	 */
	var uploadingFiles = [];

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

		if (!settings.dao) {
			logError("dao is not defined.");
			return;
		}

		initDialog();
	};

	/**
	 * Init the dialog.
	 */
	var initDialog = function() {
		/*
		 * HTML
		 */
		selectButton = $('<button type="button" class="btn btn-primary">Select file...</button>');
		clearButton = $('<button type="button" class="btn btn-warning">Clear list</button>');

		var buttonGroup = $('<div class="btn-group" role="group"></div>');
		buttonGroup.append(selectButton);
		buttonGroup.append(clearButton);

		inputFile = $('<input type="file" name="file" required="required" multiple/>');

		form = $('<form></form>');
		form.prepend(inputFile);
		form.prepend(buttonGroup);
		inputFile.hide();

		selectFileMessage = $('<p class="text-muted text-center">Select files to upload.</p>');

		uploadList = $('<div class="list-group" style="overflow-y: auto; height: 200px"></div>');
		uploadList.append(selectFileMessage);

		var html = $('<div title="Upload" style="overflow:hidden;"></div>');
		html.append(form);
		html.append("<hr />");
		html.append(uploadList);

		/*
		 * listener
		 */
		clearButton.click(clearUploadList);

		selectButton.click(function() {
			inputFile.click();
		});

		inputFile.on("change", function() {
			handleFileUpload("/", inputFile[0]);
		});

		var close = function() {
			$(this).dialog("close");
		};

		/*
		 * create dialog
		 */
		dialog = html.dialog({
			autoOpen : false,
			resizable : false,
			height : 400,
			width : 600,
			modal : true,
			buttons : {
				"Close" : close
			}
		});
	};

	/**
	 * Handle the file upload.
	 */
	var handleFileUpload = function(path, input) {

		if (!window.File || !window.FileReader || !window.FileList
				|| !window.Blob) {
			logError("The File APIs are not fully supported in this browser.");
			return;
		}

		if (!input) {
			logError("Unable to find the fileinput element.");
			return false;
		}

		if (!input.files) {
			logError("This browser does not to support the 'files' property.");
			return;
		}

		if (!input.files[0]) {
			logError("No file selected");
			return;
		}

		/*
		 * upload
		 */
		for (var i = 0; i < input.files.length; i++) {
			var file = input.files[i];
			var row = createFileRow(path, file);

			uploadingFiles.push(file);
			appendFileRow(row);

			var success = function(path, file) {
				onUploadSuccess(path, file.name);

				if (settings.onUploaded) {
					settings.onUploaded();
				}
			};

			var error = function(data, path, file) {
				logError(data);
				onUploadError(path, file.name);
			};

			settings.dao.uploadFile(success, error, path, file);
		}
	};

	/**
	 * Function to be called, when the upload was successful.
	 */
	var onUploadSuccess = function(path, filename) {
		var row = getFileRow(path, filename);
		var status = row.find("span");

		row.attr("data-status", "success");
		row.addClass("list-group-item-success");
		status.text("Success");
	};

	/**
	 * Function to be called, when the upload was failed.
	 */
	var onUploadError = function(path, filename) {
		var row = getFileRow(path, filename);
		var status = row.find("span");

		row.attr("data-status", "error");
		row.addClass("list-group-item-danger");
		status.text("Error");
	};

	/**
	 * Clear the uploadList.
	 */
	var clearUploadList = function() {
		uploadList.find("a").each(function() {
			if ($(this).attr("data-status") !== "uploading") {
				$(this).remove();
			}
		});

		if (uploadList.find("a").length === 0) {
			selectFileMessage.show();
		}
	};

	/**
	 * Append the file row to the uploadList.
	 */
	var appendFileRow = function(row) {
		selectFileMessage.hide();
		uploadList.append(row);
	}

	/**
	 * Create the file row.
	 */
	var createFileRow = function(path, file) {
		var status = $('<span class="pull-right">Uploading...</span>');
		var row = $('<a href="#" class="list-group-item">' + file.name + '</a>');
		row.append(status);
		row.attr("data-file", file.name);
		row.attr("data-path", path);
		row.attr("data-status", "uploading");

		return row;
	};

	/**
	 * Returns the row of the file in the uploadList.
	 */
	var getFileRow = function(path, filename) {
		return uploadList.find('a[data-file="' + filename + '"][data-path="'
				+ path + '"]');
	}

	/**
	 * Open the dialog.
	 */
	this.open = function() {
		dialog.dialog("open");
	}

	/**
	 * Run the constructor.
	 */
	this.construct(options);
};