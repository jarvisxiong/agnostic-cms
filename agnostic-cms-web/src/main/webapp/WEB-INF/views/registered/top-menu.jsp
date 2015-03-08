<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:url var="logoutUrl" value="/logout" htmlEscape="true" />
<spring:url var="homeUrl" value="/home" htmlEscape="true" />


<nav id="navbar-example" class="navbar navbar-default navbar-static">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="${homeUrl}">Agnostic CMS</a>
		</div>
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