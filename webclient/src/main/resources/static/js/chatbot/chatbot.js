$(function() {
	$("#textbox").keypress(function(event) {
		if (event.which == 13) {
			$("#send").click();
			event.preventDefault();
		}
	});
	$("#send").click(function() {
		var newMsg = $("#textbox").val();
		$("#textbox").val("");
		var prevMsg = $("#container").html();
		console.log(prevMsg.length);
		if (prevMsg.length != 6) {
			prevMsg = prevMsg + "<br>";
		}
		//searchViaAjax();
		var data = {}
		data["userId"] = "1104ea5f-ce7b-4211-8675-e880b9bd0ec7"; //Need to geenrate some ID for Uniqueness.
		data["messageType"] = "text";
		data["requestContent"]=[{"text":newMsg}];
		
		$.ajax({
			type : "POST",
			dataType: 'text',
			data: JSON.stringify(data),
			 headers: {
	                'Accept': 'application/json; charset=utf-8',
	                'Content-Type': 'application/json; charset=utf-8'
	         },
	         url: '/chatbot/chat', //Need to debug how to read data: in Spring. Passing as command param is not right.
			
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				var prevMsg = $("#container").html();
				console.log(prevMsg.length);
				if (prevMsg.length != 6) {
					prevMsg = prevMsg + "<br>";
				}
				var obj = JSON.parse(data);
				console.log(obj);
				
				var displayText = "";
				console.log(obj.messageType);
				if(obj.error == true){
					//Create a Internal Server Error card
					displayText+="<div class='card'>Internal Server error. Please contact your administrator<br></div>";
				}
				else{
					var newobj = obj.messageData;
					var messageType= obj.messageType;
					if(messageType== "PLAIN_TEXT"){
						displayText+="<div class='card'>";
						displayText += newobj[0].content;
						displayText += "</div>";
						
					}
					else if (messageType == "TEXT_WITH_URL" || messageType == "URL"){
						//Need to polish read Entry Information.
						for(var i=0; i< newobj.length; i++){
							displayText+="<div class='card'>";
							if(newobj[i].content != ""){
								displayText += newobj[0].content + "<br>";
							}
							if(newobj[i].image != "" && newobj[i].image != null ){
								displayText += "<img src=" +  newobj[i].image +" height='200' width='200'/><br>";
							}
							//Read Entry List
							var entryobj = newobj[i].entryList;
							for(var j=0 ; j< entryobj.length; j++){
								if(entryobj[j].buttonType == "URL"){
									var text = entryobj[j].displayText;
									displayText +=text.link(entryobj[j].uri) + "-->For reference,URL=" + entryobj[j].uri + "<br>";
									console.log(entryobj[j].uri);
									//displayText +='<a href="' + entryobj[j].uri + '">' + text + '</a>';
									
								}
								else{
									displayText += entryobj[j].displayText + "<br>";
								}
							}
							displayText += "</div>";
						}
					}
					else{
						//Read and implement Feedback for Active Learning Intent classification TODO:
						console.log("Not implemented yet");
					}
				}
				$("#container").html(prevMsg + displayText);
				$("#container").scrollTop($("#container").prop("scrollHeight"));
			},
			error : function(e) {
				console.log("ERROR: ", e);
				var displayText="<div class='card'>Internal Server error. Please contact your administrator<br></div>";
				$("#container").html(prevMsg + displayText);
				$("#container").scrollTop($("#container").prop("scrollHeight"));
			
			},
			done : function(e) {
				//
				console.log("DONE");
				//window.alert("DONE");
			}
		});
		
		$("#container").html(prevMsg + newMsg);
		$("#container").scrollTop($("#container").prop("scrollHeight"));
	});
});
