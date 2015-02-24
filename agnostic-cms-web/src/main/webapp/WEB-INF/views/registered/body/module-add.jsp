<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1><spring:message code="module.add.title" /> ${module.title}</h1>
		<form:form method="POST" action="">
			<c:forEach var="parentModule" items="${parentModules}" varStatus="loop">
				<c:set var="inputId" value="lov-input-${parentModule.id}" />				

				<div class="form-group">
					<label for="${inputId}">${parentModule.name}</label>
					<form:select id="${inputId}" path="lovValues[${parentModule.id}]" cssClass="form-control">
					
						<form:option value="" label="" />
					
						<c:set var="lov" value="${lovs[loop.index]}" />
						<c:forEach var="lovItem" items="${lov.items}">
							<t:stringifier var="optionLabel" type="${lov.type}" value="${lovItem.value}" />
							<form:option value="${lovItem.id}" label="${optionLabel}" />
						</c:forEach>
					</form:select>
				</div>
			</c:forEach>
			<c:forEach var="column" items="${columns}">
				<c:set var="inputId" value="input-${column.id}" />
				<c:set var="path" value="columnValues[${column.id}]" />
				
				<c:choose>
					<c:when test="${column.type == 'STRING' or column.type == 'LONG' or column.type == 'INT'}">
						<div class="form-group">
							<label for="${inputId}">${column.name}</label>
							<form:errors path="${path}" cssClass="text-danger" element="div" />
							<div class="text-danger">Test error</div>
							<form:input path="${path}" cssClass="form-control" id="${inputId}" placeholder="${column.defaultValue}" />
						</div>
					</c:when>
					<c:when test="${column.type == 'BOOL'}">
						<div class="checkbox">
							<div class="text-danger">Test error</div>
							<label for="${inputId}">
								<form:checkbox value="true" path="${path}" id="${inputId}" />
								${column.name}
							</label>
						</div>
					</c:when>
					<c:otherwise>undefined</c:otherwise>
				</c:choose>
			</c:forEach>
			<button type="submit" class="btn btn-default pull-right">Save</button>
		</form:form>
    </tiles:putAttribute>
</tiles:insertDefinition>