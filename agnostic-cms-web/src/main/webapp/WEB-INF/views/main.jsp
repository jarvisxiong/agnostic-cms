<%-- Top template for all Agnostic CMS pages --%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:url value="/resources/plugins/bootstrap/css/bootstrap.css" var="bootstrapCssUrl" htmlEscape="true"/>
<spring:url value="/resources/plugins/bootstrap/js/bootstrap.js" var="bootstrapJsUrl" htmlEscape="true"/>

<spring:url value="/resources/js/jquery-2.1.3.min.js" var="jqueryJsUrl" htmlEscape="true"/>

<spring:url value="/resources/plugins/font-awesome/css/font-awesome.css" var="fontAwesomeCssUrl" htmlEscape="true"/>

<spring:url value="/resources/plugins/summernote/summernote.min.js" var="summernoteJsUrl" htmlEscape="true"/>
<spring:url value="/resources/plugins/summernote/summernote.css" var="summernoteCssUrl" htmlEscape="true"/>

<spring:url value="/resources/js/cms.js" var="cmsJsUrl" htmlEscape="true"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Agnostic CMS - ${title}</title>
<link rel="stylesheet" href="${bootstrapCssUrl}">
<script src="${jqueryJsUrl}"></script>
<script src="${bootstrapJsUrl}"></script>

<%-- Only include wysiwyg stuff if it's needed  --%>
<c:if test="${wysiwygEnabled}">
	<link rel="stylesheet" href="${fontAwesomeCssUrl}">
	<link rel="stylesheet" href="${summernoteCssUrl}">
	<script src="${summernoteJsUrl}"></script>
</c:if>

<script src="${cmsJsUrl}"></script>

</head>

<body>
	<tiles:insertAttribute name="content" />
</body>

</html>