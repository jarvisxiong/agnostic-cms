<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<spring:url var="logoutUrl" value="/logout" htmlEscape="true" />
<spring:url var="homeUrl" value="/home" htmlEscape="true" />


<nav id="navbar-example" class="navbar navbar-default navbar-static">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="${homeUrl}">Agnostic CMS</a>
		</div>
		<ul class="nav navbar-nav">
			<c:forEach var="externalModule" items="${externalModules}">
				<c:if test="${externalModule.activated}">
					<spring:url value="${externalModule.url}" var="url" htmlEscape="true" />
					<li>
						<a href="${url}">
							${externalModule.name}
						</a>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li id="fat-menu" class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
						${username}
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu" aria-labelledby="drop3">
						<li role="presentation">
							<a role="menuitem" tabindex="-1" href="${logoutUrl}">
								<span class="glyphicon glyphicon-log-out"></span>
								Logout
							</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>