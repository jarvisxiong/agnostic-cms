<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:url value="/home" var="homeUrl" htmlEscape="true"/>

<ul class="nav nav-pills nav-stacked">
	<li class="${empty selectedModuleId ? 'active' : ''}">
		<a href="${homeUrl}">Home</a>
	</li>
	<c:forEach var="module" items="${modules}">
		<c:if test="${module.activated}">
			<spring:url value="/module/view/${module.id}" var="url" htmlEscape="true" />
			<li class="${selectedModuleId ==  module.id ? 'active' : ''}">
				<a href="${url}">
					${module.name}
				</a>
			</li>
		</c:if>
	</c:forEach>
</ul>