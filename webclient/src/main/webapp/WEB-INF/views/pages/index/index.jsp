<div class="container">
	<div class="row">
		<div id="tabs">
			<ul>
				<li><a href="#tabExtraction">Extraction group</a></li>
				<li><a href="#tabSearch">Search group</a></li>
			</ul>

			<div id="tabExtraction">
				<jsp:include page="tabExtraction.jsp" />
			</div>
			<div id="tabSearch">
				<jsp:include page="tabSearch.jsp" />
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		$("#tabs").tabs();
	});
</script>