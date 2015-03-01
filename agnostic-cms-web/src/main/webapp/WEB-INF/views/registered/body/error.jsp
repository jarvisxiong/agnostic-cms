<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1>
			<spring:message code="error" />
		</h1>
		
		<p>
			<spring:message code="${errorMsgCode}" />
		</p>
    </tiles:putAttribute>
</tiles:insertDefinition>