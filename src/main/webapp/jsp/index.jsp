<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
%>
<html>
	<head>
		<title>Index Page</title>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript">
			$(document).ready( function() {
				
				$("#advanced").toggle(
					function() {
						$("#search_button").show();
						$("#advanced_search").hide();
						return false;
					},
					function() {
						$("#search_button").hide();
						$("#advanced_search").show();
						return false;
					}
				).click();
			});
		</script>
	</head>
	<body>
		<div id="page">
		<jsp:include page="header.jsp" />		
			<div id="main">
				<form id="all_search" action="<%=urls.url("search")%>" method="GET">
					<div id="search_entire">
						<label for="s"> Search for Employee:</label>
						<input type="text" name="query" size="31" maxlength="255" value="" id="s" /> <a href="#advancedsearch" id="advanced"> Advanced Search</a>
						<br></br>
						<input type="submit" value ="Search" id="search_button"></input>
						<br/>
						<br/>
					</div>
					<div id="advanced_search">
						
						<label for="s"> Search by First Name:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_first"></input>
						<br/>
						
						<label for="s"> Search by Last Name:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_last"></input>
						<br/>
						
						<label for="s"> Search by Department:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_department"></input>
						<br/>
						
						<label for="s"> Search by Location:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_location"></input>
						<br></br>
						<input type="button" value ="Search" id="but"></input>
					</div>
				</form>
			</div>
			<div id="footer">
			</div>
		</div>
	</body>
</html>
