<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="type" type="com.agnosticcms.web.dto.ColumnType" required="true" %>
<%@ attribute name="value" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<c:choose>
	<c:when test="${empty value}">(None)</c:when>
	<c:when test="${type == 'STRING' or type == 'LONG' or type == 'INT'}">${value}</c:when>
	<c:when test="${type == 'BOOL'}">
		<c:choose>
			<c:when test="${value}">
				<spring:message code="yes" />
			</c:when>
			<c:otherwise>
				<spring:message code="no" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>undefined</c:otherwise>
</c:choose>