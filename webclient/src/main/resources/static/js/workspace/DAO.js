var DAO = function(options) {
	/**
	 * this.
	 */
	var root = this;

	/**
	 * Plugin settings.
	 */
	var settings = {
		icons : {
			folder : 'glyphicon glyphicon-folder-open',
			file : 'glyphicon glyphicon-file',
			workflow : 'glyphicon glyphicon-play-circle'
		}
	};

	/**
	 * Log error on the console.
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(message);
		}
	};

	/**
	 * Parse the hdfs structure to the ui structure.
	 */
	var parseRepoStructure = function(hdfsData, fileIcon) {
		var type;
		var icon;
		if (hdfsData.type === 'DIRECTORY') {
			type = 'folder';
			icon = settings.icons.folder;
		} else if (hdfsData.type === 'FILE') {
			type = 'file';
			icon = fileIcon;
		}

		var nodes = [];

		$.each(hdfsData.fileList, function(index, value) {
			nodes.push(parseRepoStructure(value, fileIcon));
		});

		var node = {
			text : hdfsData.suffix,
			id : hdfsData.path,
			type : type,
			icon : icon
		};

		if (nodes.length > 0) {
			node.nodes = nodes;
		}

		return node;
	};

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(settings, options);
	};

	this.construct(options);

	/**
	 * Get the repo file structure
	 */
	this.getRepoStructure = function(success, error) {
		uri = "./repo-ms/getHdfsStructure";
		var data = {
			location : 'REPO'
		};
		$.ajax({
			type : "POST",
			data : data,
			url : uri,
			data : data,
			success : function(data) {
				var structure = parseRepoStructure(data, settings.icons.file);
				success(structure);
			},
			error : error
		});
	};

	/**
	 * Get all workflows.
	 */
	this.getWorkflows = function(success, error) {
		uri = "./repo-ms/getHdfsStructure";
		var data = {
			location : 'WORKFLOW'
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : function(data) {
				var structure = parseRepoStructure(data,
						settings.icons.workflow);
				success(structure);
			},
			error : error
		});
	};

	/**
	 * Rename the passed target.
	 */
	this.rename = function(target, name) {
		console.log("(MOCK) rename " + target + " to " + name);
	}

	/**
	 * Remove the passed target.
	 */
	this.remove = function(target) {
		console.log("(MOCK) remove " + target);
	}

	/**
	 * Creates a new folder in the passed target.
	 */
	this.newFolder = function(target, name) {
		console.log("(MOCK) newFolder " + target + " name " + name);
	}

	/**
	 * Save workflow.
	 */
	this.saveWorkflow = function(success, error, target, workflow) {
		var data = {
				path : '/',
				location : 'WORKFLOW',
				filename : target,
				content : JSON.stringify(workflow)
		};

		var uri = "./repo-ms/storeContentInFile";
		$.ajax({
			url : uri,
			cache : false,
			data : data,
			type : 'post',
			success : success,
			error : error
		});
	}

	/**
	 * Return all target graphs.
	 */
	this.getTargetGraphs = function(success, error) {
		console.log("(MOCK) get target graphs");

		var graphs = [];
		graphs.push({
			text : 'sask',
			id : 'sask',
			type : 'db',
			icon : 'glyphicon glyphicon-hdd'
		});

		success(graphs);
	}

	/**
	 * Upload files.
	 */
	this.uploadFiles = function(success, error, path, input) {
		if (!window.File || !window.FileReader || !window.FileList
				|| !window.Blob) {
			logError('The File APIs are not fully supported in this browser.');
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

		var formData = new FormData();
		formData.append('path', path);
		formData.append('location', "REPO");

		for (var i = 0; i < input.files.length; i++) {
			formData.append('files', input.files[i]);
		}

		uri = "./repo-ms/storeFiles";
		$.ajax({
			url : uri,
			cache : false,
			contentType : false,
			processData : false,
			data : formData,
			type : 'post',
			success : success,
			error : error
		});
	};
};