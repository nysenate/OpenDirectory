<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	String error_message = (String)request.getAttribute("errorMessage");
	Person user = (Person)request.getAttribute("user");
	String title = (String)request.getAttribute("title");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=title%></title>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","application.css")%>" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript" src="<%=urls.url("js","application.js")%>"></script>
	</head>
	<body>
		<div id="page">
			<div id="header">
				<a href="http://www.nysenate.gov">
					<img src="http://www.nysenate.gov/sites/all/themes/nys/images/nyss_logo.png" id="logo" />
				</a>
			</div>
			<div id="nav_bar">
			</div>
			<div id="main" style="margin-top:50px;">
				<h2 style="margin-left:30px;">Access OpenDirectory from outside the Senate network</h2>
				<div id="main_center">					
					<% if(error_message != null) { %>
						<div id="error">
							<%=error_message %>
							<br></br>
						</div>
					<% } %>
					
					<div id="edit_form_field">
						<form action="<%=urls.url("external","login")%>" method="POST">
							<ol>
								<li>
									<label for="login_name">Username:</label>
									<input type="text" name="name" size="31" maxlength="255" value="" id="login_name"/>
								</li>
								<li>
									<label for="login_pword">Password:</label>
								<input type="password" name="password" size="31" maxlength="255" value="" id="login_pword"/>	
								</li>					
							</ol>
							<input type="submit" value="Login" id="login_button"></input>
						</form>
					</div>
				</div>
			</div>
			<div id="footer">
			</div>
		</div>
	</body>
</html>