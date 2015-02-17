<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="row">
	<div class="col-md-12">
		<tiles:insertAttribute name="top-menu" />
	</div>
</div>

<div class="row">
	<div class="col-md-4">
		<tiles:insertAttribute name="left-menu" />
	</div>
	<div class="col-md-8">
		<tiles:insertAttribute name="body" />
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<tiles:insertAttribute name="footer" />
	</div>
</div>