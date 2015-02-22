<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1><spring:message code="module.add.title" /> ${module.title}</h1>
		<form>
		
			<%-- <c:forEach var="parentModule" items="${parentModules}">
				<th>${parentModule.name}</th>
			</c:forEach> --%>
			<c:forEach var="column" items="${columns}">
				<c:set var="inputId" value="input-${column.id}" />
				
				<c:choose>
					<c:when test="${column.type == 'STRING' or column.type == 'LONG' or column.type == 'INT'}">
						<div class="form-group">
							<label for="${inputId}">${column.name}</label>
							<input type="text" class="form-control" id="${inputId}" placeholder="${column.defaultValue}">
						</div>
					</c:when>
					<c:when test="${column.type == 'BOOL'}">
						<div class="checkbox">
							<label for="${inputId}">
								<input id="${inputId}" type="checkbox">
								${column.name}
							</label>
						</div>
					</c:when>
					<c:otherwise>undefined</c:otherwise>
				</c:choose>
			</c:forEach>
			<button type="submit" class="btn btn-default pull-right">Save</button>
		</form>
    </tiles:putAttribute>
</tiles:insertDefinition>