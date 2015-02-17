<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<div class="row">
			<div class="col-md-12">
				<h1>
					Welcome to Agnostic CMS!
				</h1>
			</div>
		</div>
    </tiles:putAttribute>
</tiles:insertDefinition>