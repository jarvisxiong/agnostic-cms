<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<spring:url var="editUrl" value="/module/edit/${module.id}/${itemId}" htmlEscape="true" />

 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1><spring:message code="module.viewsingle.title" /> ${module.title}</h1>
    	
    	<dl class="dl-horizontal">
			<c:forEach var="parentModule" items="${parentModules}" varStatus="loop">
				<dt>${parentModule.name}</dt>
				<dd>${lovItems[loop.index]}</dd>
			</c:forEach>
			<c:forEach var="column" items="${columns}">
				<dt>${column.name}</dt>
				<dd><t:displayer type="${column.type}" value="${row[column.nameInDb]}" /></dd>
			</c:forEach>
		</dl>
		
		<c:if test="${activable}">
			<c:set var="activated" value="${row['activated']}" />
		
			<div class="pull-left">
				<spring:url var="activationToggleUrl" value="/module/activation/${itemId}/${not activated}" />
				<a class="btn btn-danger" href="${activationToggleUrl}" role="button">
					<spring:message code="module.activation.${activated ? 'deactivate' : 'activate'}" />
				</a>
			</div>
		</c:if>
		<a role="button" href="${editUrl}" class="btn btn-default pull-right">Edit</a>
    </tiles:putAttribute>
</tiles:insertDefinition>