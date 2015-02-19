<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
 
<tiles:insertDefinition name="registered.main">
    <tiles:putAttribute cascade="true" name="body">
    	<div class="row">
			<div class="col-md-12">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>TH1</th>
							<th>TH2</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>TD1</td>
							<td>TD2</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
    </tiles:putAttribute>
</tiles:insertDefinition>