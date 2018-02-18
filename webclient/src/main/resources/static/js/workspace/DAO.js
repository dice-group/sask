var DAO = function(options) {
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
	var logError = function (message) {
		if (window.console) {
			window.console.error(message);
		}
	};
	
	/**
	 * Parse the hdfs structure to the ui structure.
	 */
	var parseRepoStructure = function(hdfsData) {
		var type;
		if(hdfsData.type === 'DIRECTORY') {
			type = 'folder';
		} else if(hdfsData.type === 'FILE') {
			type = 'file';
		}
		
		var nodes = [];
		
		$.each(hdfsData.fileList, function( index, value ) {
			nodes.push(parseRepoStructure(value));
		});
			
		var node = {
			text : hdfsData.suffix,
			id : hdfsData.path,
			type : type
		};
		
		if(nodes.length > 0) {
			node.nodes = nodes;
		}
		
		return node;
	};
	
	/**
     * Constructor
     */
    this.construct = function(options){
        $.extend(settings , options);
    };
    
    this.construct(options);
    
    /**
     * Get the repo file structure
     */
    this.getRepoStructure = function(success, error) {
    	var data = {
    			location: "REPO"
    	};
    	
    	uri = "./repo-ms/getHdfsStructure";
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
	 * Rename the passed target.
	 */
	this.rename = function(target, name) {
		console.log("rename " + target + " to " + name);
	}
	
	/**
	 * Remove the passed target.
	 */
	this.remove = function(target) {
		console.log("remove " + target);
	}
	
	/**
	 * Creates a new folder in the passed target.
	 */
	this.newFolder = function(target, name) {
		console.log("newFolder " + target + " name " + name);
	}
	
	/**
	 * Save workflow.
	 */
	this.saveWorkflow = function(success, error, target, workflow) {
		console.log("save workflow " + target + " " + workflow);
	}
	
	/**
	 * Upload files.
	 */
	this.uploadFiles = function(success, error, path, input) {
		if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
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
            url: uri,
            cache: false,
            contentType: false,
            processData: false,
            data: formData,                         
            type: 'post',
            success: success,
            error: error
 });
	};
};