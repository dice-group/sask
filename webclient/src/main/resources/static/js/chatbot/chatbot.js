/**
 * JQuery plugin for the chatbot.
 * 
 * @author Kevin Haack, Divya
 */
;
((function($, window, document) {

	/* global jQuery, console */
	"use strict";

	/**
	 * The plugin name.
	 */
	var pluginName = "chatbot";

	var _default = {};

	/**
	 * The send button.
	 */
	var sendButton;

	/**
	 * The textfield.
	 */
	var textfield;

	/**
	 * The chat list.
	 */
	var chatList;

	/**
	 * The chat body.
	 */
	var chatBody;

	_default.settings = {
		chatListTemplate : "<div class=\"chat\"></div>",
		feedbackTemplate : "<div class=\"card left text-center\"><div><span>Was this helpful?</span></div><div class=\"message\"></div></div>",
		feedbackButtonYesTemplate : "<button border-style=\"solid\" type=\"button\" class=\"btn btn-primary\">Yes</button>",
		feedbackButtonNoTemplate : "<button border-style=\"solid\" type=\"button\" class=\"btn btn-primary\">No</button>",
		rightTemplate : "<div class=\"card right\"><div class=\"messageHead\"><span class=\"pull-right\">You</span></div><div class=\"message\"></div></div>",
		leftTemplate : "<div class=\"card left\"><div class=\"messageHead\"><span>Chatbot</span></div><div class=\"message\"></div></div>",
		footerTemplate : "<div class=\"chat-footer form-group\"></div>",
		bodyTemplate : "<div class=\"chat-body\"></div>",
		textfieldTemplate : "<input type=\"text\" class=\"form-control chat-textfield\"/>",
		sendTemplate : "<input type=\"submit\" class=\"btn btn-primary chat-send\" value=\"Send\"/>",
		messageInternalError : "Internal Server error. Please contact your administrator",
		messageNotOnline : "Sorry, I'm not online now.",
		messageFeedbackThanks : "Thank you for your feedback",
		errorClass : "error",
		messageClass : "message",
		onBigMessage : null,
		dao : null
	};

	/**
	 * logging function
	 */
	var logError = function(message) {
		if (window.console) {
			window.console.error(pluginName + ": " + message);
		}
	};

	var Chatbot = function(element, options) {
		this.$element = $(element);
		this.elementId = element.id;
		this.styleId = this.elementId + "-style";

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

		textfield.on("keypress", function(e) {
			if (e.which === 13 && textfield.val().trim()) {
				self.onSendMessage();
			}
		});

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
		}, "slow");
	};

	/**
	 * Create a right card.
	 */
	Chatbot.prototype.addRightCard = function(message) {
		var card = $(this.options.rightTemplate);
		card.find("." + this.options.messageClass).text(message);
		this.addCard(card);
		return card;
	};

	/**
	 * Create a left card.
	 */
	Chatbot.prototype.addLeftCard = function(message) {
		var card = $(this.options.leftTemplate);
		card.find("." + this.options.messageClass).text(message);
		this.addCard(card);
		return card;
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
		this.addRightCard(message);

		// is chatbot registered
		if (!this.options.dao.getDiscoverer().isChatbotDiscovered()) {
			this.addErrorMessage(this.options.messageNotOnline);
			return;
		}

		// create data to send
		var data = {};
		data["userId"] = "1104ea5f-ce7b-4211-8675-e880b9bd0ec7";

		// Need need to generate some ID for Uniqueness.
		data["messageType"] = "text";
		data["requestContent"] = [ {
			"text" : message
		} ];

		this.sendData(data, message);
	};

	/**
	 * Add a new error message from the passed data.
	 */
	Chatbot.prototype.addErrorMessage = function(text) {
		var card = this.addLeftCard(text);
		card.addClass(this.options.errorClass);
	};

	/**
	 * Add a new big message.
	 */
	Chatbot.prototype.addBigMessage = function(messageData) {
		var card = $(this.options.leftTemplate);
		var messageDiv = card.find("." + this.options.messageClass);

		for (var i = 0; i < messageData.length; i++) {
			if (messageData[i].classPredicted !== "") {
				messageDiv.append(messageData[i].classPredicted + "<br />");	
			}
			if (messageData[i].content !== "") {
				messageDiv.append(messageData[i].content + "<br />");
			}

			if (messageData[i].image !== "" && messageData[i].image !== null) {
				messageDiv.append("<img src=" + messageData[i].image
						+ " height=\"200\" width=\"200\"/><br>");
			}

			// Read Entry List
			var entryobj = messageData[i].entryList;
			for (var j = 0; j < entryobj.length; j++) {
				if (entryobj[j].buttonType === "URL") {
					var text = entryobj[j].displayText;
					messageDiv.append(text.link(entryobj[j].uri) + "<br>");

				} else {
					messageDiv(entryobj[j].displayText + "<br>");
				}
			}

		}

		this.addCard(card);
	};

	/**
	 * On feedback click.
	 */
	Chatbot.prototype.onFeedbackClick = function(feedback, button) {
		this.addLeftCard(this.options.messageFeedbackThanks);
		var message = button.attr("name");

		this.options.dao.sendChatFeedback(message, feedback);
		button.parent().remove();
	};

	/**
	 * Add a new feedback message
	 */
	Chatbot.prototype.addFeedback = function(requestText) {
		var self = this;
		var card = $(this.options.feedbackTemplate);
		var yes = $(this.options.feedbackButtonYesTemplate);
		var no = $(this.options.feedbackButtonNoTemplate);

		yes.attr("name", requestText);
		yes.click(function() {
			self.onFeedbackClick("positive", yes);
		});

		no.attr("name", requestText);
		no.click(function() {
			self.onFeedbackClick("negative", no);
		});

		card.append(yes);
		card.append("&nbsp;&nbsp;");
		card.append(no);

		chatList.append(card);
		chatBody.animate({
			scrollTop : chatBody.height()
		}, "slow");
	};

	/**
	 * Add a new messages from the passed data.
	 */
	Chatbot.prototype.addMessage = function(data, requestMessage) {
		var dataObject = JSON.parse(data);
		var messageData = dataObject.messageData;
		if (dataObject.error === true) {
			if ((typeof messageData != "undefined" && messageData != null && messageData.length != null && messageData.length > 0) && messageData[0].classPredicted !== "") {
				this.addErrorMessage(messageData[0].classPredicted + ", Answer: " + this.options.messageInternalError);
			}
			else{
				this.addErrorMessage(this.options.messageInternalError);
			}
			return;
		}

		//var messageData = dataObject.messageData;
		var messageType = dataObject.messageType;

		if (messageType === "PLAIN_TEXT") {
			if (messageData[0].classPredicted !== "") {
				this.addLeftCard(messageData[0].classPredicted + ", Answer: " + messageData[0].content);	
			}
			else {
				this.addLeftCard(messageData[0].content);
			}
		} else if (messageType === "TEXT_WITH_URL" || messageType === "URL") {
			if (this.options.onBigMessage) {
				this.options.onBigMessage();
			}

			this.addBigMessage(messageData);
		}

		this.addFeedback(requestMessage);
	};

	/**
	 * Send the passed data.
	 */
	Chatbot.prototype.sendData = function(data, message) {
		var self = this;
		var onSuccess = function(result) {
			self.addMessage(result, message);
		};

		var onError = function(result) {
			self.addErrorMessage(self.options.messageInternalError);
		};

		this.options.dao.sendChatMessage(data, onSuccess, onError);
	};

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

			if (typeof options === "string") {
				if (!_this) {
					logError("Not initialized, can not call method : "
							+ options);
				} else if (!$.isFunction(_this[options])
						|| options.charAt(0) === "_") {
					logError("No such method : " + options);
				} else {
					if (!(args instanceof Array)) {
						args = [ args ];
					}
					result = _this[options].apply(_this, args);
				}
			} else if (typeof options === "boolean") {
				result = _this;
			} else {
				$.data(this, pluginName, new Chatbot(this, $.extend(true, {},
						options)));
			}
		});

		return result || this;
	};
})(jQuery, window, document));