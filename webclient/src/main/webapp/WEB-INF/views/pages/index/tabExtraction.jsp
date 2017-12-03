<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<h1>Extraction prototype</h1>
<form>
	<div class="form-group">
		    <label for="email">File:</label> <input id="ex-fileinput"
			name="input-b1" type="file" class="file form-control">
	</div>
	<div class="radio">
		  <label><input type="radio" name="optradio">FOX</label>
	</div>
	<div class="radio">
		  <label><input type="radio" name="optradio">BOA</label>
	</div>
	<div class="radio">
		  <label> <input type="radio" name="optradio">RDF Fred
		</label>
	</div>
	<button type="submit" class="btn btn-default">Start</button>
</form>