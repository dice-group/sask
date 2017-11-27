<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<head>
<title>Home</title>
<link rel="stylesheet"
	href="<c:url value="/bootstrap/3.3.7/css/bootstrap.min.css" />" />
<script src="<c:url value="/js/jquery-3.2.1.min.js" />"></script>

<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/css/fileinput.min.css"
	media="all" rel="stylesheet" type="text/css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/plugins/piexif.min.js"
	type="text/javascript"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/plugins/sortable.min.js"
	type="text/javascript"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/plugins/purify.min.js"
	type="text/javascript"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/fileinput.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-fileinput/4.4.5/js/locales/LANG.js"></script>
<script>
	$(document).ready(function() {
		$("#input-b1").fileinput();
	});
</script>

</head>

<body>

	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">SASK</a>
			</div>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="active"><a href="./index">Home</a></li>
					<li><a href="./datatest">Status</a></li>
					<li><a href="./datatest">Settings</a></li>
					<li><a href="./datatest">REST test</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			<h1>Extraction prototype</h1>
			<form>
				<div class="form-group">
					    <label for="email">File:</label> <input id="input-b1"
						name="input-b1" type="file" class="file form-control">
				</div>
				<div class="radio">
					  <label><input type="radio" name="optradio">FOX</label>
				</div>
				<div class="radio">
					  <label><input type="radio" name="optradio">BOA</label>
				</div>
				<div class="radio">
					  <label><input type="radio" name="optradio">RDF Fred</label>
				</div>
				<button type="submit" class="btn btn-default">Start</button>
			</form>
		</div>
	</div>
</body>
</html>