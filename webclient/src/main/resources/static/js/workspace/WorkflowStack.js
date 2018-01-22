var WorkflowStack = function(options){
	
	/**
	 * this.
	 */
	var root = this;
	
	/**
	 * The saved workflows.
	 */
	var workflows = [];
	
	/**
	 * The current position in the stack.
	 */
	var index = -1;
	
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
     * Saves the passed workflow
     */
	this.saveWorkflow = function(workflow) {
		if(index < workflows.length - 1) {
			workflows = workflows.slice(0, index); 
		}
		
		workflows.push(workflow);
		index = workflows.length - 1;
	};
	
	/**
	 * Returns the last workflow
	 */
	this.getLastWorkflow = function() {
		index -= 1;
		var workflow = workflows[index];
		
		if(!workflow) {
			logError("there is no last workflow");
			return;
		}
		
		return workflow;
	};
	
	/**
	 * Return the next workflow.
	 */
	this.getNextWorkflow = function() {
		index += 1;
		var workflow = workflows[index];
		
		if(!workflow) {
			logError("there is no next workflow");
			return;
		}
		
		return workflow;
	};
	
	/**
	 * Clears the stack.
	 */
	this.clear = function() {
		workflows = [];
		index = -1;
	};
	
	/**
	 * Has a last workflow.
	 */
	this.hasLast = function () {
        return index > 0;
	};
	
	/**
	 * Has a next workflow.
	 */
	this.hasNext = function () {
        return index < (workflows.length - 1);
	};
};