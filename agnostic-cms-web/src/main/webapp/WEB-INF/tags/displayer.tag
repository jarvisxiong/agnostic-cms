<%-- displays a value according to the column type --%>
<%@ tag trimDirectiveWhitespaces="true" %>

<%-- type of the value's column --%>
<%@ attribute name="type" type="com.agnosticcms.web.dto.ColumnType" required="true" %>
<%-- value to show --%>
<%@ attribute name="value" required="true" %>
<%-- display width for images --%>
<%@ attribute name="displayWidth" %>
<%-- variable to set the value to --%>
<%@ attribute name="var" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%-- should the HTML entities be escaped --%>
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
	<%-- if there is no variable to set, output result directly --%>
	<c:when test="${empty var}">
		<c:out value="${displayerResult}" escapeXml="${escapeOutput}" />
	</c:when>
	<c:otherwise>
		<%-- tmp var is needed just to satisfy eclipse validator --%>
		<c:set var="tmpPageContext" value="${pageContext}" />
		<%-- setting the passed atribute to displayerResult value --%>
		${tmpPageContext.request.setAttribute(var, displayerResult)}
	</c:otherwise>
</c:choose>