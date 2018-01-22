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
			success : success,
			error : error
		});
	};
};