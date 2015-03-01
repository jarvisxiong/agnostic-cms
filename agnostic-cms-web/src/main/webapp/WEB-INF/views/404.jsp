<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<tiles:insertDefinition name="nonregistered.main">
    <tiles:putAttribute name="content">
    	<div class="container">
			<h1>
				<spring:message code="resource.notfound" />
			</h1>
		</div>
    </tiles:putAttribute>
</tiles:insertDefinition>