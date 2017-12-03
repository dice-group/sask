<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">

<head>
<title>${pageTitle}</title>
<!-- ~~~~~~ bootstrap ~~~~~~ -->
<link rel="stylesheet"
	href="<c:url value="/bootstrap/3.3.7/css/bootstrap.min.css" />" />
<!--~~~~~~  jquery ~~~~~~ -->
<script src="<c:url value="/jquery/jquery-3.2.1.min.js" />"
	type="text/javascript"></script>
<!--~~~~~~  jquery ui ~~~~~~ -->
<script src="/jquery-ui-1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="/jquery-ui-1.12.1/jquery-ui.css">
<!--~~~~~~  flowchart ~~~~~~ -->
<script src="/flowchart-js/jquery.flowchart.js"></script>
<link rel="stylesheet" href="/flowchart-js/jquery.flowchart.css">
<!--~~~~~~  bootstrap-treeview ~~~~~~ -->
<script src="/bootstrap-treeview/bootstrap-treeview.js"></script>
<link rel="stylesheet" href="/bootstrap-treeview/bootstrap-treeview.css">
<!--~~~~~~  file input ~~~~~~ -->
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
<!--~~~~~~  page scripts ~~~~~~ -->
<c:forEach items="${scripts}" var="script">
	<script src="<c:url value="${script}" />" type="text/javascript"></script>
</c:forEach>
<!--~~~~~~  page styles ~~~~~~ -->
<c:forEach items="${styles}" var="style">
	<link rel="stylesheet" href="${style}" />
</c:forEach>
</head>
<body>
	<jsp:include page="navbar.jsp" />
	<jsp:include page="${contentPage}" />
	<jsp:include page="footer.jsp" />
</body>
</html>