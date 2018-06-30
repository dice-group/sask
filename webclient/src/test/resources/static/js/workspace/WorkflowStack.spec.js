describe("A workflow stack", function() {
	it("should start empty", function() {
		var stack = new WorkflowStack({});
		expect(false).toBe(stack.hasNext());
	});

	it("should start marked as unsaved", function() {
		var stack = new WorkflowStack({});
		expect(false).toBe(stack.isSaved());
	});

	it("should save the previous workflow", function() {
		var stack = new WorkflowStack({});
		stack.saveWorkflow("first");
		stack.saveWorkflow("second");
		expect("first").toBe(stack.getLastWorkflow());
	});

	it("should save the next workflow", function() {
		var stack = new WorkflowStack({});
		stack.saveWorkflow("first");
		stack.saveWorkflow("second");

		stack.getLastWorkflow();

		expect("second").toBe(stack.getNextWorkflow());
	});

	it("should mark the first workflow as saved", function() {
		var stack = new WorkflowStack({});
		stack.saveWorkflow("first");
		expect(true).toBe(stack.isSaved());
	});

	it("should not mark new workflows as saved", function() {
		var stack = new WorkflowStack({});
		stack.saveWorkflow("first");
		stack.saveWorkflow("second");
		expect(false).toBe(stack.isSaved());
	});

	it("should be able to mark a workflow as saved", function() {
		var stack = new WorkflowStack({});
		stack.saveWorkflow("first");
		expect(true).toBe(stack.isSaved());

		stack.saveWorkflow("second");
		stack.setSaved();
		expect(true).toBe(stack.isSaved());
	});
});
