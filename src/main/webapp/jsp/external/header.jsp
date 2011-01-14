<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>NYSS OpenDirectory</title>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","application.css")%>" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript" src="<%=urls.url("js","external.js")%>"></script>
	</head>
	<body>
		<div id="page">
			<div id="header">
				<a href="http://www.nysenate.gov">
					<img src="http://www.nysenate.gov/sites/all/themes/nys/images/nyss_logo.png" id="logo" />
				</a>
			</div>
			<div id="nav_bar">
				Not a Senate user? 
				<ul>
					<li><a href="<%=urls.url("external","login")%>" class="nav">Login</a><div class="sep">or</div></li>
					<li><a href="<%=urls.url("external","register")%>" class="nav">Register</a></li>
				</ul>
			</div>	