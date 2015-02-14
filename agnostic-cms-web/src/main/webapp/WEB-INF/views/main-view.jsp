<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:url value="/resources/css/bootstrap.css" var="bootstrapCssUrl" htmlEscape="true"/>

<spring:url value="/resources/js/bootstrap.js" var="bootstrapJsUrl" htmlEscape="true"/>
<spring:url value="/resources/js/jquery-2.1.3.min.js" var="jqueryJsUrl" htmlEscape="true"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Agnostic CMS - ${title}</title>
<link rel="stylesheet" href="${bootstrapCssUrl}">
<script src="${jqueryJsUrl}"></script>
<script src="${bootstrapJsUrl}"></script>
</head>

<body>
	<div class="container">
		<div class="row">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
</body>

</html>