<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="errorScope" value="${requestScope['org.springframework.validation.BindingResult.moduleInput']}" />

 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<h1><spring:message code="${editMode ? 'module.edit.title' : 'module.add.title'}" /> ${module.title}</h1>
		<form:form method="POST" action="" commandName="moduleInput">
			<c:forEach var="parentModule" items="${parentModules}" varStatus="loop">
				<c:set var="inputId" value="lov-input-${loop.index}" />
				<c:set var="path" value="lovValues[${loop.index}]" />
				<c:set var="errorClass" value="${errorScope.hasFieldErrors(path) ? ' has-error' : ''}" />

				<div class="form-group${errorClass}">
					<label class="control-label" for="${inputId}">${parentModule.name}</label>
					<form:errors path="${path}" cssClass="text-danger pull-right" element="div" />
					<form:select id="${inputId}" path="${path}" cssClass="form-control">
					
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
			
				<c:if test="${(editMode and column.showInEdit) or (not editMode and column.showInAdd)}">
					<c:set var="inputId" value="input-${column.id}" />
					<c:set var="path" value="columnValues[${column.id}]" />
					<c:set var="errorClass" value="${errorScope.hasFieldErrors(path) ? ' has-error' : ''}" />
					
					<c:choose>
						<c:when test="${column.type == 'STRING' or column.type == 'LONG' or column.type == 'INT'}">
							<div class="form-group${errorClass}">
								<label class="control-label" for="${inputId}">${column.name}</label>
								<form:errors path="${path}" cssClass="text-danger pull-right" element="div" />
								<form:input path="${path}" cssClass="form-control" id="${inputId}" />
							</div>
						</c:when>
						<c:when test="${column.type == 'BOOL'}">
							<div class="${errorClass}">
								<div class="checkbox">
									<form:errors path="${path}" cssClass="text-danger pull-right" element="div" />
									<label for="${inputId}">
										<form:checkbox value="true" path="${path}" id="${inputId}" />
										${column.name}
									</label>
								</div>
							</div>
						</c:when>
						<c:when test="${column.type == 'ENUM'}">
							<div class="form-group${errorClass}">
								<label class="control-label" for="${inputId}">${column.name}</label>
								<form:errors path="${path}" cssClass="text-danger pull-right" element="div" />
								<form:select id="${inputId}" path="${path}" cssClass="form-control">
									<form:option value="" label="" />
									<form:options items="${fn:split(column.typeInfo, ',')}" />
								</form:select>
							</div>
						</c:when>
						<c:otherwise>undefined</c:otherwise>
					</c:choose>
				</c:if>
			</c:forEach>
			<button type="submit" class="btn btn-default pull-right"><spring:message code="module.add.save" /></button>
		</form:form>
    </tiles:putAttribute>
</tiles:insertDefinition>