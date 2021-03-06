<%-- view for module elements in a form of a table --%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>

<spring:url var="addUrl" value="/module/add/${module.id}" htmlEscape="true" />

 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
		<h1>${module.title}</h1>
		<c:choose>
			<c:when test="${not empty rows}">
				<table class="table table-bordered">
					<thead>
						<tr>
							<c:forEach var="parentModule" items="${parentModules}">
								<th>${parentModule.name}</th>
							</c:forEach>
							<c:forEach var="column" items="${columns}">
								<c:if test="${column.showInList}">
									<th>${column.name}</th>
								</c:if>
							</c:forEach>
							<th></th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="row" items="${rows}">
						
							<c:set var="rowId" value="${row['id']}" />
						
							<spring:url var="viewUrl" value="/module/view/${module.id}/${rowId}" htmlEscape="true" />
							<spring:url var="editUrl" value="/module/edit/${module.id}/${rowId}" htmlEscape="true" />
							<spring:url var="deleteUrl" value="/module/delete/${module.id}/${rowId}" htmlEscape="true" />
						
							<tr>
								<%-- output lov columns first --%>
								<c:forEach var="foreignKeyName" items="${foreignKeyNames}" varStatus="loop">
									<td>${lovs[loop.index][row[foreignKeyName]]}</td>
								</c:forEach>
								<%-- other columns afterwards --%>
								<c:forEach var="column" items="${columns}">
									<c:if test="${column.showInList}">
										<td><t:displayer type="${column.type}" value="${row[column.nameInDb]}" displayWidth="50" /></td>
									</c:if>
								</c:forEach>
								
								<td><a href="${viewUrl}"><spring:message code="module.view.view" /></a></td>
								<td><a href="${editUrl}"><spring:message code="module.view.edit" /></a></td>
								<td><a href="${deleteUrl}"><spring:message code="module.view.remove" /></a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				<h4 class="text-center"><spring:message code="module.view.empty" /></h4>
			</c:otherwise>
		</c:choose>
		
		
		<div class="pull-right">
			<a class="btn btn-default" href="${addUrl}" role="button"><spring:message code="module.add.button" /></a>
		</div>
    </tiles:putAttribute>
</tiles:insertDefinition>