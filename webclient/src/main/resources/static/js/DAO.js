/**
 * Data access object for the REST interfaces.
 * 
 * @author Nilanjan
 * @author Kevin Haack
 * 
 */
;
var DAO = function(options) {
	/**
	 * this.
	 */
	var root = this;

	/**
	 * Discoverer.
	 */
	var discoverer = new Discoverer({
		dao : this
	});

	/**
	 * Plugin settings.
	 */
	var settings = {
		icons : {
			folder : "glyphicon glyphicon-folder-open",
			file : "glyphicon glyphicon-file",
			workflow : "glyphicon glyphicon-play-circle"
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
		if (hdfsData.type === "DIRECTORY") {
			type = "folder";
			icon = settings.icons.folder;
		} else if (hdfsData.type === "FILE") {
			type = "file";
			icon = settings.icons.file;
		}

		var nodes = [];

		$.each(hdfsData.fileList, function(index, value) {
			nodes.push(parseRepoStructure(value));
		});

		var node = {
			text : hdfsData.suffix,
			id : hdfsData.path,
			type,
			icon
		};

		if (type === "folder") {
			node.nodes = nodes;
		}

		return node;
	};

	/**
	 * Return the repo serviceId.
	 */
	var getRepoServiceUri = function() {
		if (!discoverer.getRepo()) {
			return;
		}

		return "./" + discoverer.getRepo().serviceId + "/";
	};
	
	var getDatabaseServiceId = function() {
		if (!discoverer.isDatabaseDiscovered()) {
			return;
		}

		return "./" + discoverer.getDatabase().serviceId + "/";
	};

	/**
	 * Return the executer service id.
	 */
	var getExecuterServiceUri = function() {
		if (!discoverer.getExecuter()) {
			return;
		}

		return "./" + discoverer.getExecuter().serviceId + "/";
	};
	
	/**
	 * Return the chatbot service id.
	 */
	var getChatbotServiceUri = function() {
		if (!discoverer.isChatbotDiscovered()) {
			return;
		}

		return "./" + discoverer.getChatbot().serviceId + "/";
	};
	
/** fetch data from Query graph to display in table* */
	
	this.queryGraph = function(onSuccess)
	{
		var success = function(data) {
			var parsedData = JSON.parse(data);
			var result = [];
			
			if("results" in parsedData
					&& "bindings" in parsedData.results) {
				var bindingsArray = parsedData.results.bindings;
				bindingsArray.forEach(function(element) {
					
					var s = element.s.value;
					var p = element.p.value;
					var o = element.o.value;
					result.push({
						s,
						p,
						o
					});
				}); 
			}
			onSuccess(result);
		};
		
		var data = {
			limit : "30"
		};
		
		$.ajax({
			type: "GET",
			url: getDatabaseServiceId() + "queryDefaultGraph",
			data,
			success
		});
  };

	/**
	 * Parse the hdfs workflows structure to the ui structure.
	 */
	var parseWorkflowStructure = function(hdfsData) {
		var type;
		var icon;
		if (hdfsData.type === "DIRECTORY") {
			type = "folder";
			icon = settings.icons.folder;
		} else if (hdfsData.type === "FILE") {
			type = "workflow";
			icon = settings.icons.workflow;
		}

		var nodes = [];

		$.each(hdfsData.fileList, function(index, value) {
			nodes.push(parseWorkflowStructure(value));
		});

		var node = {
			text : hdfsData.suffix,
			id : hdfsData.path,
			type,
			icon
		};

		if (nodes.length > 0) {
			node.nodes = nodes;
		}

		return node;
	};

	/**
	 * Return the discoverer.
	 */
	this.getDiscoverer = function() {
		return discoverer;
	};

	/**
	 * Constructor
	 */
	this.construct = function(options) {
		$.extend(settings, options);

		if (typeof Discoverer !== "function") {
			logError("'Discoverer' not initialized.");
			return;
		}
	};

	this.construct(options);

	/**
	 * Discover the registered microservices.
	 */
	this.discoverMicroservices = function(success, error) {
		var url = "./discoverMicroservices";

		$.ajax({
			type : "GET",
			url,
			success,
			error
		});
	};
	
	/**
	 * Send the feedback to the chatbot.
	 */
	this.sendChatFeedback = function(message, feedback) {
		var data = {};
		data["query"] = message;
		data["feedback"] = feedback;
		
		$.ajax({
			type : "POST",
			dataType: "text",
			data: JSON.stringify(data),
			url: "/chatbot/feedback",
			timeout: 100000,
			contentType: "application/json",
			async: true		
		});
	};
	
	/**
	 * Send the passed data to the chatbot.
	 */
	this.sendChatMessage = function(data, success, error) {
		var url = getChatbotServiceUri() + "chat";
		
		$.ajax({
			type : "POST",
			dataType : "text",
			data : JSON.stringify(data),
			contentType : "application/json",
			url, // Need to debug how
			// to read data: in
			// Spring. Passing
			// as command param
			// is not right.
			timeout : 100000,
			success,
			error,
			async : false
		});
	};

	/**
	 * Send the passed workflow to the executer.
	 */
	this.executeWorkflow = function(success, error, workflow) {
		var url = getExecuterServiceUri() + "executeWorkflow";

		$.ajax({
			type : "POST",
			data : JSON.stringify(workflow),
			contentType : "application/json",
			url,
			success,
			error
		});
	};

	/**
	 * Get the repo file structure
	 */
	this.getRepoStructure = function(success, error) {
		var url = getRepoServiceUri() + "getHdfsStructure";
		var data = {
			location : "repo"
		};
		$.ajax({
			type : "POST",
			data,
			url,
			error,
			success(data) {
				var structure = parseRepoStructure(data);
				success(structure);
			}
		});
	};

	/**
	 * Get all workflows.
	 */
	this.getWorkflows = function(success, error) {
		var url = getRepoServiceUri() + "getHdfsStructure";
		var data = {
			location : "workflow"
		};

		$.ajax({
			type : "POST",
			url,
			data,
			error,
			success(data) {
				var structure = parseWorkflowStructure(data);
				success(structure);
			}
		});
	};

	/**
	 * Get the workflow.
	 */
	this.getWorkflow = function(success, error, target) {
		var url = getRepoServiceUri() + "readFile";
		var data = {
			location : "workflow",
			path : target
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Rename the passed target.
	 */
	this.renameRepo = function(success, error, from, to) {
		var uri = getRepoServiceUri() + "rename";
		var data = {
			location : "repo",
			from,
			to
		};

		$.ajax({
			type : "POST",
			url : uri,
			data,
			success,
			error
		});
	};

	/**
	 * Rename the passed workflow.
	 */
	this.renameWorkflow = function(success, error, from, to) {
		var url = getRepoServiceUri() + "rename";
		var data = {
			location : "workflow",
			from,
			to
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Rename the passed target.
	 */
	this.renameWorkflow = function(success, error, from, to) {
		var url = getRepoServiceUri() + "rename";
		var data = {
			location : "workflow",
			from,
			to
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Remove the passed target from the repo.
	 */
	this.removeFromRepo = function(success, error, target) {
		var url = getRepoServiceUri() + "delete";
		var data = {
			location : "repo",
			path : target
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Remove the passed target from the workflows.
	 */
	this.removeFromWorkflows = function(success, error, target) {
		var url = getRepoServiceUri() + "delete";
		var data = {
			location : "workflow",
			path : target
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Creates a new folder in the passed target.
	 */
	this.createDirectory = function(success, error, target, name) {
		if (!target.endsWith("/")) {
			target = target + "/";
		}

		var url = getRepoServiceUri() + "createDirectory";
		var data = {
			location : "repo",
			path : target + name
		};

		$.ajax({
			type : "POST",
			url,
			data,
			success,
			error
		});
	};

	/**
	 * Save workflow.
	 */
	this.saveWorkflow = function(success, error, target, workflow) {
		if (target.startsWith("/")) {
			target = target.substring(1);
		}

		var data = new FormData();
		data.append("path", "/");
		data.append("location", "workflow");
		data.append("filename", target);
		data.append("file", JSON.stringify(workflow));

		var url = getRepoServiceUri() + "storeContentInFile";
		$.ajax({
			url,
			cache : false,
			contentType : false,
			processData : false,
			data,
			type : "post",
			success,
			error
		});
	};

	/**
	 * Return all target graphs.
	 */
	this.getTargetGraphs = function(success, error) {
		var graphs = [];
		graphs.push({
			text : "sask",
			id : "sask",
			type : "db",
			icon : "glyphicon glyphicon-hdd"
		});

		success(graphs);
	};

	/**
	 * Upload file.
	 */
	this.uploadFile = function(success, error, path, file) {
		var data = new FormData();
		data.append("path", path);
		data.append("location", "repo");
		data.append("file", file);

		var url = getRepoServiceUri() + "storeFile";
		$.ajax({
			url,
			cache : false,
			contentType : false,
			processData : false,
			data,
			type : "post",
			success() {
				if (success) {
					success(path, file);
				}
			},
			error(data) {
				if (error) {
					error(data, path, file);
				}
			}
		});
	};
};