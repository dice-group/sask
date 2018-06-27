(function($, window, document) {

	/* global jQuery, console */
	'use strict';

	/**
	 * The plugin name.
	 */
	var pluginName = 'chatbot';

	var _default = {};

	/**
	 * The send button.
	 */
	var sendButton = undefined;

	/**
	 * The textfield.
	 */
	var textfield = undefined;

	/**
	 * The chat list.
	 */
	var chatList = undefined;

	/**
	 * The chat body.
	 */
	var chatBody = undefined;

	_default.settings = {
		chatListTemplate : "<ul class=\"chat\"></ul>",
		rightTemplate : "<li class=\"right\"><p>You</p></li>",
		leftTemplate : "<li class=\"left\"><p>Chatbot</p></li>",
		footerTemplate : "<div class=\"chat-footer form-group\"></div>",
		bodyTemplate : "<div class=\"chat-body\"></div>",
		textfieldTemplate : "<input type=\"text\" class=\"form-control chat-textfield\"/>",
		sendTemplate : "<input type=\"submit\" class=\"btn btn-primary chat-send\" value=\"Send\"/>",
		errorClass : "error"
	};

	var Chatbot = function(element, options) {
		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + '-style';

		this.init(options);

		return {
			options : this.options,
			init : $.proxy(this.init, this)
		};
	};

	/**
	 * Init.
	 */
	Chatbot.prototype.init = function(options) {
		this.options = $.extend({}, _default.settings, options);

		this.initBody();
		this.initFooter();
		this.initChat();
	};

	/**
	 * Init the chat.
	 */
	Chatbot.prototype.initChat = function() {
		var self = this;

		sendButton.click(function() {
			if (textfield.val().trim()) {
				self.onSendMessage();
			}
		});
	};

	/**
	 * Add the passed card.
	 */
	Chatbot.prototype.addCard = function(card) {
		chatList.append(card);
		chatBody.animate({
			scrollTop : chatBody.height()
		}, 'slow');
	};

	/**
	 * Method called on message send.
	 */
	Chatbot.prototype.onSendMessage = function() {
		if (!textfield.val().trim()) {
			return;
		}

		var message = textfield.val().trim();
		textfield.val("");

		// add card
		var card = $(this.options.rightTemplate);
		card.append(message);
		this.addCard(card);

		// create data to send
		var data = {}
		data["userId"] = "1104ea5f-ce7b-4211-8675-e880b9bd0ec7";

		// Need need to generate some ID for Uniqueness.
		data["messageType"] = "text";
		data["requestContent"] = [ {
			"text" : message
		} ];

		this.sendData(data);
	};

	/**
	 * Addd a new error message from the passed data.
	 */
	Chatbot.prototype.addErrorMessage = function(data) {
		var card = $(this.options.leftTemplate);
		card.append("Internal Server error. Please contact your administrator");
		card.addClass(this.options.errorClass);
		this.addCard(card);
	};

	/**
	 * Add a new messages from the passed data.
	 */
	Chatbot.prototype.addMessage = function(data) {
		var dataObject = JSON.parse(data);

		if (dataObject.error === true) {
			this.addErrorMessage(data);
			return;
		}

		var messageData = dataObject.messageData;
		var messageType = dataObject.messageType;

		if (messageType === "PLAIN_TEXT") {
			var card = $(this.options.leftTemplate);
			card.append(messageData[0].content);
			this.addCard(card);
			return;
		}

		if (messageType === "TEXT_WITH_URL" || messageType === "URL") {
			// Need to polish read Entry
			// Information.
			var displayText = "";
			for (var i = 0; i < messageData.length; i++) {
				displayText += "<div class='card'>";
				if (messageData[i].content != "") {
					displayText += messageData[0].content + "<br>";
					// why this <br> getting added?
				}
				if (messageData[i].image != "" && messageData[i].image != null) {
					displayText += "<img src=" + newobj[i].image
							+ " height='200' width='200'/><br>";
				}
				// Read Entry List
				var entryobj = messageData[i].entryList;
				for (var j = 0; j < entryobj.length; j++) {
					if (entryobj[j].buttonType == "URL") {
						var text = entryobj[j].displayText;
						displayText += text.link(entryobj[j].uri)
								+ "-->For reference,URL=" + entryobj[j].uri
								+ "<br>";

					} else {
						displayText += entryobj[j].displayText + "<br>";
					}
				}
				displayText += "</div>";
			}

			chatList.append(displayText);
			return;
		}
	}

	/**
	 * Send the passed data.
	 */
	Chatbot.prototype.sendData = function(data) {
		var self = this;
		var onSuccess = function(result) {
			self.addMessage(result);
		}

		var onError = function(result) {
			self.addErrorMessage(result);
		}

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
			timeout : 100000,
			success : onSuccess,
			error : onError
		})
	}

	/**
	 * Init the body
	 */
	Chatbot.prototype.initBody = function() {
		var self = this;

		chatList = $(this.options.chatListTemplate);
		chatBody = $(this.options.bodyTemplate);

		chatBody.append(chatList);
		this.$element.append(chatBody);
	};

	/**
	 * Init the footer of the chatbot
	 */
	Chatbot.prototype.initFooter = function() {
		var self = this;

		var footer = $(this.options.footerTemplate);
		textfield = $(this.options.textfieldTemplate);
		sendButton = $(this.options.sendTemplate);

		footer.append(textfield);
		footer.append(sendButton);

		this.$element.append(footer);
	}

	/**
	 * logging function
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(pluginName + ": " + message);
		}
	};

	/**
	 * Add plugin.
	 */
	$.fn[pluginName] = function(options, args) {
		// Prevent against multiple instantiations,
		// handle updates and method calls
		var result;

		this.each(function() {
			var _this = $.data(this, pluginName);

			if (typeof options === 'string') {
				if (!_this) {
					logError('Not initialized, can not call method : '
							+ options);
				} else if (!$.isFunction(_this[options])
						|| options.charAt(0) === '_') {
					logError('No such method : ' + options);
				} else {
					if (!(args instanceof Array)) {
						args = [ args ];
					}
					result = _this[options].apply(_this, args);
				}
			} else if (typeof options === 'boolean') {
				result = _this;
			} else {
				$.data(this, pluginName, new Chatbot(this, $.extend(true, {},
						options)));
			}
		});

		return result || this;
	};
})(jQuery, window, document);
0