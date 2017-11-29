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
		data["message"] = newMsg;
		//window.alert("Before ajax1");
		//window.alert($("#textbox").val());
		$.post( "conversation" ,data , function(data){
			//window.alert(data);//Publish answer in Content area instead of message box. Crude solution for now, Fix later
			var prevMsg = $("#container").html();
			console.log(prevMsg.length);
			if (prevMsg.length != 6) {
				prevMsg = prevMsg + "<br>";
			}
			$("#container").html(prevMsg + data);
			/*success : function(data) {
				console.log("SUCCESS: ", data);
				window.alert(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				window.alert(e);
			},
			done : function(e) {
				//
				console.log("DONE");
				window.alert("DONE");
			}*/
		
		});
		/*$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "conversation?message=" + $("#textbox").val(), //Need to debug how to read data: in Spring. Passing as command param is not right.
			data : {message: "Hello"},//JSON.stringify(data),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				window.alert(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				window.alert(e);
			},
			done : function(e) {
				//
				console.log("DONE");
				window.alert("DONE");
			}
		});*/
		
		$("#container").html(prevMsg + newMsg);
		$("#container").scrollTop($("#container").prop("scrollHeight"));
	});
});