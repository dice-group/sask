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
    	uri = "./repo-ms/getRepoStructure";
		$.ajax({
			type : "POST",
			url : uri,
			success : function(data) {
				var json = jQuery.parseJSON(data);
				var structure = parseRepoStructure(json);
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
};