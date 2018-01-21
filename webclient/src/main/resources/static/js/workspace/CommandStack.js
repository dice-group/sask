var CommandStack = function(options){
	
	var root = this;
	
	var commands = [];
	
	var index = -1;
	
	var settings = {};
	
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
	 * Do the command.
	 */
	this.doCommand = function(commandArray) {
		if(!commandArray || !commandArray['do'] || typeof commandArray['do'] !== 'function') {
			logError("the command does not have a 'do' function");
		}
		
		var command = commandArray['do'];
		command();
		
		if(commandArray['undo'] && typeof commandArray['undo'] == 'function') {
			commands.push(commandArray);
			index = commands.length - 1;
		}
	};
	
	/**
	 * Undo the last command.
	 */
	this.undoCommand = function() {
		var commandArray = commands[index];
		
		if(!commandArray) {
			logError("no commands for undo");
		}
		
		var command = commandArray['undo'];
		command();
		index -= 1;
	};
	
	/**
	 * Redo the next command.
	 */
	this.redoCommand = function() {
		
		var commandArray = commands[index + 1];
		
		if(!commandArray) {
			logError("no commands for redo");
		}
		
		var command = commandArray['do'];
		
		command();
		index += 1;
	};
	
	/**
	 * Clear the commands.
	 */
	this.clear = function() {
		commands = [];
		index = -1;
	};
	
	/**
	 * Has command for undo.
	 */
	this.hasUndo = function () {
        return index !== -1;
	};
	
	/**
	 * Has command for redo.
	 */
	this.hasRedo = function () {
        return index < (commands.length - 1);
	};
};