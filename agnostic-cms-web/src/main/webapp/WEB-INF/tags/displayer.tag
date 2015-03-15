<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="type" type="com.agnosticcms.web.dto.ColumnType" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="displayWidth" %>
<%@ attribute name="var" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<c:set var="escapeOutput" value="true" />

<c:choose>
	<c:when test="${empty value}">
		<c:set var="displayerResult" value="(None)" />
	</c:when>
	<c:when test="${type == 'STRING' or type == 'LONG' or type == 'INT' or type == 'ENUM' or type == 'DECIMAL'}">
		<c:set var="displayerResult" value="${value}" />
	</c:when>
	<c:when test="${type == 'HTML'}">
		<c:set var="displayerResult" value="${value}" />
		<c:set var="escapeOutput" value="false" />
	</c:when>
	<c:when test="${type == 'BOOL'}">
		<c:choose>
			<c:when test="${value}">
				<spring:message var="displayerResult" code="yes" />
			</c:when>
			<c:otherwise>
				<spring:message var="displayerResult" code="no" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${type == 'IMAGE'}">
		<spring:url var="imgUrl" value="/content-resources${value}" htmlEscape="true" />
		<img src="${imgUrl}" width="${displayWidth}" >
	</c:when>
	<c:otherwise>
		<c:set var="displayerResult" value="undefined displayer value" />
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${empty var}">
		<c:out value="${displayerResult}" escapeXml="${escapeOutput}" />
	</c:when>
	<c:otherwise>
		<%-- tmp var is needed just to satisfy eclipse validator.. --%>
		<c:set var="tmpPageContext" value="${pageContext}" />
		${tmpPageContext.request.setAttribute(var, displayerResult)}
	</c:otherwise>
</c:choose>