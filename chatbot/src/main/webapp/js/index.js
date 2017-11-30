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
	         url: '/chat', //Need to debug how to read data: in Spring. Passing as command param is not right.
			
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
				var link=obj[0].URI;
				console.log(link);
				var displayText="";
				var elementName="Open in DBPedia";
				if (typeof obj[0].thumbnail == "undefined")
					console.log("Thumbnail is not present");
				else
					displayText += "<img src=" +  obj[0].thumbnail +" height='200' width='200'/><br>";
				if (typeof obj[0].comment == "undefined")
					console.log("Comment is not present");
				else
					displayText += obj[0].comment +"<br>";
				displayText +=elementName.link(link) + "-->For reference,URL=" + link;
				
				$("#container").html(prevMsg + displayText);
				
			},
			error : function(e) {
				console.log("ERROR: ", e);
			
			},
			done : function(e) {
				//
				console.log("DONE");
				window.alert("DONE");
			}
		});
		
		$("#container").html(prevMsg + newMsg);
		$("#container").scrollTop($("#container").prop("scrollHeight"));
	});
});