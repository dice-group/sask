$(function() {
	$("#textbox").keypress(function(event) {
		if (event.which === 13) {
		var newMsg = $("#textbox").val();
		newMsg = newMsg.trim();
		if(newMsg.length > 0){
			$("#send").click();
			event.preventDefault();
		}
		else{
			$("#textbox").val("");	
		}
	}});
	$("#send").click(function() {
		var newMsg = $("#textbox").val();
		newMsg = newMsg.trim();//eliminate whitespace on either side
		$("#textbox").val("");
		var prevMsg = $("#container").html();
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
			contentType: "application/json",
	         url: "/chatbot/chat", //Need to debug how to read data: in Spring. Passing as command param is not right.
			timeout : 100000 })
			.done(function(data){ 
				var prevMsg = $("#container").html();
				if (prevMsg.length != 6) {
					prevMsg = prevMsg + "<br>";
				}
				var obj = JSON.parse(data);
				
				var displayText = "";
				if(obj.error === true){
					//Create a Internal Server Error card
					displayText+="<div class='card'>Internal Server error. Please contact your administrator<br></div>";
				}
				else{
					var newobj = obj.messageData;
					var messageType= obj.messageType;
					if(messageType=== "PLAIN_TEXT"){
						displayText+="<div class='card'>";
						displayText += newobj[0].content;
						displayText += "</div>";
						
					}
					else if (messageType=== "TEXT_WITH_URL" || messageType === "URL"){
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
									displayText +=text.link(entryobj[j].uri) + "<br>";
									
								}
								else{
									displayText += entryobj[j].displayText + "<br>";
								}
							}
							displayText += "</div>";
						}
					}
					
				}
				//Add div for feedback
				displayText += "<br><br><div>Was this helpful?<br><button name=\""+ newMsg + "\" onClick=\"onYesClick(this)\" border-style=\"solid\" type=\"button\" class=\"btn btn-primary\">Yes</button>&nbsp;&nbsp;<button name=\""+ newMsg + "\" onClick=\"onNoClick(this)\" type=\"button\" class=\"btn btn-primary\">No</button></div>";
				$("#container").html(prevMsg + displayText);
				$("#container").scrollTop($("#container").prop("scrollHeight"));
			})
			.fail(function(data){
				var displayText="<div class='card'>Internal Server error. Please contact your administrator<br></div>";
				$("#container").html(prevMsg + displayText);
				$("#container").scrollTop($("#container").prop("scrollHeight"));
		});
		
		$("#container").html(prevMsg + newMsg);
		$("#container").scrollTop($("#container").prop("scrollHeight"));
		$("#send").prop("disabled",true);
	});
});

function sendRequest(query,feedback){
	var data={};
	data["query"] = query;
	data["feedback"] = feedback;
	$.ajax({
		type : "POST",
		dataType: "text",
		data: JSON.stringify(data),
		url: "/chatbot/feedback",
		timeout: 100000,
		contentType: "application/json",
		async: true,		
	});
	
};
function onYesClick(obj){
	var closestDiv=$(obj).closest("div");
	closestDiv.html("Thank you for your feedback");
	var query= $(obj).attr("name");
	sendRequest(query, "positive");
};

function onNoClick(obj){
	var closestDiv=$(obj).closest("div");
	closestDiv.html("Thank you for your feedback");
	//TODO:Send Ajax to Server on Negative feedback
	var query= $(obj).attr("name");
	sendRequest(query, "negative");
};

$(document).ready(function() {
    $("#textbox").on("keyup", function() {
    var textBoxContent = document.getElementById("textbox").value;
    textBoxContent = textBoxContent.trim();
    if(0 === textBoxContent.length){
        $("#send").prop("disabled",true);}
     else{
     	$("#send").prop("disabled",false);
     }
    }).trigger("keyup");
    

});

