<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="type" type="com.agnosticcms.web.dto.ColumnType" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="var" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<c:choose>
	<c:when test="${empty value}">
		<c:set var="stringifierResult" value="(None)" />
	</c:when>
	<c:when test="${type == 'STRING' or type == 'LONG' or type == 'INT'}">
		<c:set var="stringifierResult" value="${value}" />
	</c:when>
	<c:when test="${type == 'BOOL'}">
		<c:choose>
			<c:when test="${value}">
				<spring:message var="stringifierResult" code="yes" />
			</c:when>
			<c:otherwise>
				<spring:message var="stringifierResult" code="no" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:set var="stringifierResult" value="undefined" />
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${empty var}">
		<c:out value="${stringifierResult}" />
	</c:when>
	<c:otherwise>
		<%-- tmp var is needed just to satisfy eclipse validator.. --%>
		<c:set var="tmpPageContext" value="${pageContext}" />
		${tmpPageContext.request.setAttribute(var, stringifierResult)}
	</c:otherwise>
</c:choose>