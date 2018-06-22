$(document).ready(function() {
	/**
	 * Send.
	 */
	function send() {
		console.log("button clicked");
		var newMsg = $("#textbox").val();
		newMsg = newMsg.trim();// eliminate whitespace on
		// either side
		$("#textbox").val("");
		
		// this boolean could be passed via the chatbot ms
		// could say that the messasage is big
		var isInSmall = true;
		var shouldBeBig = true;
		if(isInSmall && shouldBeBig) {
			toBigView();
		}
		
		var prevMsg = $("#chatWindow .chat").html();
		if (prevMsg.length != 6) {
			prevMsg = prevMsg + "<br>";
		}
		// searchViaAjax();
		var data = {}
		data["userId"] = "1104ea5f-ce7b-4211-8675-e880b9bd0ec7";
		// Need to geenrate some ID for Uniqueness.
		data["messageType"] = "text";
		data["requestContent"] = [ {
			"text" : newMsg
		} ];

		$.ajax({
			type : "POST",
			dataType : 'text',
			data : JSON.stringify(data),
			contentType : "application/json",
			url : "/chatbot/chat", // Need to debug how
			// to read data: in
			// Spring. Passing
			// as command param
			// is not right.
			timeout : 100000
		}).done(
						function(data) {
							var prevMsg = $("#chatWindow .chat")
									.html();
							if (prevMsg.length != 6) {
								prevMsg = "<li class=\"left clearfix\">"
										+ prevMsg + "</li>";
							}
							var obj = JSON.parse(data);

							var displayText = "";
							if (obj.error === true) {
								// Create a Internal Server
								// Error card
								displayText += "<div class='card'>Internal Server error. Please contact your administrator<br></div>";
							} else {
								var newobj = obj.messageData;
								var messageType = obj.messageType;
								if (messageType === "PLAIN_TEXT") {
									displayText += "<li class='left clearfix'>";
									displayText += newobj[0].content;
									displayText += "</li>";

								} else if (messageType === "TEXT_WITH_URL"
										|| messageType === "URL") {
									// Need to polish read Entry
									// Information.
									for (var i = 0; i < newobj.length; i++) {
										displayText += "<div class='card'>";
										if (newobj[i].content != "") {
											displayText += newobj[0].content
													+ "<br>";// why
											// this
											// <br>getting
											// added
											// ?
										}
										if (newobj[i].image != ""
												&& newobj[i].image != null) {
											displayText += "<img src="
													+ newobj[i].image
													+ " height='200' width='200'/><br>";
										}
										// Read Entry List
										var entryobj = newobj[i].entryList;
										for (var j = 0; j < entryobj.length; j++) {
											if (entryobj[j].buttonType == "URL") {
												var text = entryobj[j].displayText;
												displayText += text
														.link(entryobj[j].uri)
														+ "-->For reference,URL="
														+ entryobj[j].uri
														+ "<br>";

											} else {
												displayText += entryobj[j].displayText
														+ "<br>";
											}
										}
										displayText += "</div>";
									}
								}

							}
							$("#chatWindow .chat").html(
									prevMsg + displayText);
							$("#chatWindow").scrollTop(
									$("#chatWindow").prop(
											"scrollHeight"));
						})
				.fail(
						function(data) {
							var displayText = "<div class='card'>Internal Server error. Please contact your administrator<br></div>";
							$("#chatWindow .chat").html(
									prevMsg + displayText);
							$("#chatWindow").scrollTop(
									$("#chatWindow").prop(
											"scrollHeight"));
						});

		$("#chatWindow .chat").html(prevMsg + newMsg);
		$("#chatWindow").scrollTop(
				$("#chatWindow").prop("scrollHeight"));
		$("#send").prop("disabled", true);
	}
	
	/**
	 * Switch the view.
	 */
	function switchView() {
		var chatWindow = $("#chatWindow");
		var isSmall = chatWindow.hasClass("chat-smallView");
		
		if(isSmall) {
			toBigView();
		} else {
			toSmallView();
		}
	}
	
	/**
	 * switch the chat view to big
	 */
	function toBigView() {
		var chatWindow = $("#chatWindow");
		chatWindow.removeClass("chat-smallView");
		chatWindow.addClass("chat-bigView");
		
		$("#ex-workspace").hide();
		chatWindow.detach().appendTo("#chat-big");
	}
	
	/**
	 * switch the chat view to small
	 */
	function toSmallView() {
		var chatWindow = $('#chatWindow');
		chatWindow.addClass("chat-smallView");
		chatWindow.removeClass("chat-bigView");
		
		$("#ex-workspace").show();
		chatWindow.detach().appendTo("#tabExtraction .row");
	}
	
	/**
	 * On slideDown click.
	 * 
	 * @returns
	 */
	$("#chat-slideDown").click(function(){
        $("panel-body").slideDown();
    });
	
	/**
	 * On Send click.
	 */
	$("#send").click(send);
	
	/**
	 *  On switch the view.
	 */
	$("#chat-switch").click(switchView);
	
	/**
	 * On Send with enter.
	 * 
	 * @param event
	 * @returns
	 */
	$("#textbox").keypress(function(event) {
		if (event.which === 13) {
			var newMsg = $("#textbox").val();
			newMsg = newMsg.trim();
			
			if (newMsg.length > 0) {
				send();
				event.preventDefault();
			} else {
				$("#textbox").val("");
			}
		}
	});
	
	/**
	 * Disable and enable send button
	 * 
	 * @returns
	 */
	$("#textbox").on("keyup", function() {
		var textBoxContent = document.getElementById("textbox").value;
		textBoxContent = textBoxContent.trim();
		
		if (0 === textBoxContent.length) {
			$("#send").prop("disabled", true);
		} else {
			$("#send").prop("disabled", false);
		}
	}).trigger("keyup");

});
