<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1>
			<spring:message code="error" />
		</h1>
		
		<p>
			<c:choose>
				<c:when test="${not empty errorMsgCode}">
					<spring:message code="${errorMsgCode}" />
				</c:when>
				<c:otherwise>
					${errorMsg}
				</c:otherwise>
			</c:choose>
		</p>
    </tiles:putAttribute>
</tiles:insertDefinition>