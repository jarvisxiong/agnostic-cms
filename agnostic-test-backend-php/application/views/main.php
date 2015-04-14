<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Price multiplier</title>
<link rel="stylesheet" href="<?=base_url()?>public/plugins/bootstrap/css/bootstrap.css">
<script src="<?=base_url()?>public/js/jquery-2.1.3.min.js"></script>
<script src="<?=base_url()?>public/plugins/bootstrap/js/bootstrap.js"></script>

</head>

<body>
	<div class="container">
		<nav id="navbar-example" class="navbar navbar-default navbar-static">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="/cms/home">Agnostic CMS</a>
				</div>
				<ul class="nav navbar-nav">
					<? foreach($external_modules as $external_module) : ?>
						<li class="<?=$selected_external_module_id == $external_module->id ? 'active' : ''?>">
							<a href="/cms<?=$external_module->url?>">
								<?=$external_module->name?>
							</a>
						</li>
					<? endforeach; ?>
				</ul>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right">
						<li id="fat-menu" class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">
								<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
								<?=$username?>
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menu" aria-labelledby="drop3">
								<li role="presentation">
									<a role="menuitem" tabindex="-1" href="/cms/logout">
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
		
		<div class="row">
			<div class="col-md-2">
				<ul class="nav nav-pills nav-stacked">					
					<? foreach($modules as $module) : ?>
						<li>
							<a href="/cms/module/view/<?=$module->id?>">
								<?=$module->name?>
							</a>
						</li>
					<? endforeach; ?>
				</ul>
			</div>
			<div class="col-md-10">
				<h2>Product price multiplier</h2>
				
				<div class="row">
					<div class="col-md-3">
						<form method="POST" action="">
							<div class="form-group<?=validation_errors() ? ' has-error' : ''?>">
								<label class="control-label" for="multiplier-input">Multiply prices by: </label>
								<? if(validation_errors()): ?>
									<div class="text-danger pull-right"><?=validation_errors()?></div>
								<? endif; ?>
								<input type="text" class="form-control" id="multiplier-input" name="multiplier" value="<?=set_value("multiplier")?>" />
							</div>
							
							<div>
								<button type="submit" class="btn btn-default pull-right">Multiply</button>
							</div>
						</form>
					</div>
				</div>
				
				
				
				<h3>Products</h3>
				
				<table class="table table-striped">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Price</th>
						</tr>
					</thead>
					<tbody>
						<? foreach($products as $product) : ?>
							<tr>
								<td><?=$product->id?></td>
								<td><?=$product->name?></td>
								<td><?=$product->price?></td>
							</tr>
						<? endforeach; ?>
					</tbody>
				</table>
				
			</div>
		</div>
	</div>
	
	<footer>
		<div class="container">
			<br />
		</div>
	</footer>
</body>

</html>