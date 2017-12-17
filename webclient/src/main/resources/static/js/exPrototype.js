function ex_sendToFred(text) {
	extractor = $("input[name=ex-extractor]:checked").val();

	if(extractor == "fred") {
		uri = "./fred-ms/extractSimple";
	} else if(extractor == "fox") {
		uri = "./fox-ms/extractSimple";
	} else {
		uri = "./spotlight-ms/extractSimple";
	}
	
	// parameter
	parameters = {
		input : text
	};

	// request
	$.ajax({
		type : "POST",
		data : parameters,
		url : uri,
		success : function(data) {
			$("#ex-result").val(data);
		},
		error : function(e, textStatus, errorThrown) {
			alert('Error in processing: ' + textStatus);
		}
	});

}

function ex_onFileLoaded(e) {
	ex_sendToFred(e.target.result);
}

function ex_submitfile() {
	if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
		alert('The File APIs are not fully supported in this browser.');
		return false;
	}

	input = document.getElementById('ex-fileinput');

	if (!input) {
		alert("Unable to find the fileinput element.");
		return false;
	}

	if (!input.files) {
		alert("This browser does not to support the 'files' property.");
		return;
	}

	if (!input.files[0]) {
		alert("No file selected");
		return;
	}

	file = input.files[0];
	
	filereader = new FileReader();
	filereader.onload = ex_onFileLoaded;
	filereader.readAsText(file);

	return false;
}
