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
	 * Parse the hdfs repo structure to the ui structure.
	 */
	var parseRepoStructure = function(hdfsData) {
		var type;
		var icon;
		if (hdfsData.type === 'DIRECTORY') {
			type = 'folder';
			icon = settings.icons.folder;
		} else if (hdfsData.type === 'FILE') {
			type = 'file';
			icon = settings.icons.file;
		}

		var nodes = [];

		$.each(hdfsData.fileList, function(index, value) {
			nodes.push(parseRepoStructure(value));
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
	 * Parse the hdfs workflows structure to the ui structure.
	 */
	var parseWorkflowStructure = function(hdfsData) {
		var type;
		var icon;
		if (hdfsData.type === 'DIRECTORY') {
			type = 'folder';
			icon = settings.icons.folder;
		} else if (hdfsData.type === 'FILE') {
			type = 'workflow';
			icon = settings.icons.workflow;
		}

		var nodes = [];

		$.each(hdfsData.fileList, function(index, value) {
			nodes.push(parseWorkflowStructure(value));
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
	 * Discover the registered microservices.
	 */
	this.discoverMicroservices = function(success, error) {
		uri = "./discoverMicroservices";

		$.ajax({
			type : "GET",
			url : uri,
			success : success,
			error : error
		});
	};

	/**
	 * Get the repo file structure
	 */
	this.getRepoStructure = function(success, error) {
		uri = "./repo-ms/getHdfsStructure";
		var data = {
			location : 'repo'
		};
		$.ajax({
			type : "POST",
			data : data,
			url : uri,
			success : function(data) {
				var structure = parseRepoStructure(data);
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
			location : 'workflow'
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : function(data) {
				var structure = parseWorkflowStructure(data);
				success(structure);
			},
			error : error
		});
	};

	/**
	 * Get the workflow.
	 */
	this.getWorkflow = function(success, error, target) {
		uri = "./repo-ms/readFile";
		var data = {
			location : 'workflow',
			path : target
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	};

	/**
	 * Rename the passed target.
	 */
	this.renameRepo = function(success, error, from, to) {
		uri = "./repo-ms/rename";
		var data = {
			location : 'repo',
			from : from,
			to : to
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Rename the passed workflow.
	 */
	this.renameWorkflow = function(success, error, from, to) {
		uri = "./repo-ms/rename";
		var data = {
			location : 'workflow',
			from : from,
			to : to
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Rename the passed target.
	 */
	this.renameWorkflow = function(success, error, from, to) {
		uri = "./repo-ms/rename";
		var data = {
			location : 'workflow',
			from : from,
			to : to
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Remove the passed target from the repo.
	 */
	this.removeFromRepo = function(success, error, target) {
		uri = "./repo-ms/delete";
		var data = {
			location : 'repo',
			path : target
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Remove the passed target from the workflows.
	 */
	this.removeFromWorkflows = function(success, error, target) {
		uri = "./repo-ms/delete";
		var data = {
			location : 'workflow',
			path : target
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Creates a new folder in the passed target.
	 */
	this.createDirectory = function(success, error, target, name) {
		if(!target.endsWith("/")) {
			target = target + "/";
		}
		
		uri = "./repo-ms/createDirectory";
		var data = {
			location : 'repo',
			path : target + name
		};

		$.ajax({
			type : "POST",
			url : uri,
			data : data,
			success : success,
			error : error
		});
	}

	/**
	 * Save workflow.
	 */
	this.saveWorkflow = function(success, error, target, workflow) {
		var data = {
			path : '/',
			location : 'workflow',
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
	 * Upload file.
	 */
	this.uploadFile = function(success, error, path, file) {
		var formData = new FormData();
		formData.append('path', path);
		formData.append('location', "repo");

		formData.append('files', file);

		uri = "./repo-ms/storeFiles";
		$.ajax({
			url : uri,
			cache : false,
			contentType : false,
			processData : false,
			data : formData,
			type : 'post',
			success : function(data) {
				if (success) {
					success(data, path, file);
				}
			},
			error : function(data) {
				if (error) {
					error(data, path, file);
				}
			}
		});
	};
};