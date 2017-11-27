<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<head>
<title>REST test</title>
<link rel="stylesheet"
	href="<c:url value="/bootstrap/3.3.7/css/bootstrap.min.css" />" />
<script src="<c:url value="/js/jquery-3.2.1.min.js" />"></script>
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
					<li><a href="./index">Home</a></li>
					<li><a href="#">Status</a></li>
					<li><a href="#">Settings</a></li>
					<li class="active"><a href="./datatest">REST test</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			<h1>REST test</h1>
			<div>
				I got this data from the other microservice as root stuff: <b>${rootstuff}</b>
			</div>
			<div>
				I got this data from the other microservice as string: <b>${hello}</b>
			</div>
			<div>
				I got this data from the other microservice as json: <b>${hellojson.message}</b>
			</div>
		</div>
	</div>
</body>
</html>