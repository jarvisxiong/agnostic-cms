<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="container">
	<tiles:insertAttribute name="top-menu" />
	
	<div class="row">
		<div class="col-md-2">
			<tiles:insertAttribute name="left-menu" />
		</div>
		<div class="col-md-10">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
</div>

<tiles:insertAttribute name="footer" />