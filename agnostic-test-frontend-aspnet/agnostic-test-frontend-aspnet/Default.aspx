<%@ Page Language="C#" Inherits="agnostictestfrontend.Default" %>

<%@ Assembly name="System" %>
<%@ Assembly name="System.Data" %>
<%@ Assembly name="Npgsql" %>

<%@ Import Namespace="System.Collections.Generic" %>
<%@ Import Namespace="agnostictestfrontend" %>

<!DOCTYPE HTML>
<!--
	Design:
	Photon by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Photon by HTML5 UP</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<!--[if lte IE 8]><script src="css/ie/html5shiv.js"></script><![endif]-->
		<script src="js/jquery.min.js"></script>
		<script src="js/jquery.scrolly.min.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/init.js"></script>
		<noscript>
			<link rel="stylesheet" href="css/skel.css" />
			<link rel="stylesheet" href="css/style.css" />
			<link rel="stylesheet" href="css/style-xlarge.css" />
		</noscript>
		<!--[if lte IE 9]><link rel="stylesheet" href="css/ie/v9.css" /><![endif]-->
		<!--[if lte IE 8]><link rel="stylesheet" href="css/ie/v8.css" /><![endif]-->
	</head>
	<body>
		<!-- Header -->
			<section id="header">
				<div class="inner">
					<span class="icon major fa-cloud"></span>
					<h1>Hi, this is an example of ASP.NET frontend application backed by Agnostic CMS</h1>
					<p>ASP.NET here is powered by Mono - free and open source project</p>
					<ul class="actions">
						<li><a href="#one" class="button scrolly">Discover</a></li>
					</ul>
				</div>
			</section>
				
			<section id="three" class="main style1 special">
				<div class="container">
					
						<%
						// Image resource base, that targets Agnostic CMS core directly
						string resourceBase = "http://localhost:8080/cms/content-resources";
						// Obtaining categories and the products they contain
						List<Category> categories = GetDBData();
						foreach(Category category in categories) {
						%>
							<%-- Output for a single category --%>
							<header class="major">
								<h2><%=category.name%></h2>
							</header>
							<p><%=category.description%></p>
							
							<div class="row 150%">
							<%-- Iteration over all products of current category --%>
							<% foreach(Product product in category.products) { %>
							
								<div class="4u 12u$(medium)">
								    <span class="image fit">
								    	<img src="<%=resourceBase%><%=product.image%>" alt="<%=product.name%>" />
								    </span>
									<h3><%=product.name%></h3>
									<%=product.description%>
									<ul class="actions">
										<li><a href="#" class="button">More</a></li>
									</ul>
								</div>
							<% } %>
							</div>
						<% } %>
				</div>
			</section>

			<!-- Footer -->
			<section id="footer">
				<ul class="icons">
					<li><a href="#" class="icon alt fa-twitter"><span class="label">Twitter</span></a></li>
					<li><a href="#" class="icon alt fa-facebook"><span class="label">Facebook</span></a></li>
					<li><a href="#" class="icon alt fa-instagram"><span class="label">Instagram</span></a></li>
					<li><a href="#" class="icon alt fa-github"><span class="label">GitHub</span></a></li>
					<li><a href="#" class="icon alt fa-envelope"><span class="label">Email</span></a></li>
				</ul>
				<ul class="copyright">
					<li>&copy; Untitled</li><li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
				</ul>
			</section>

	</body>
</html>