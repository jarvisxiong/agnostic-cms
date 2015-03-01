<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<tiles:insertDefinition name="nonregistered.main">
    <tiles:putAttribute name="content">
    	<div class="container">
			<h4>
				<spring:message code="${messageCode}" />
			</h4>
		</div>
    </tiles:putAttribute>
</tiles:insertDefinition>