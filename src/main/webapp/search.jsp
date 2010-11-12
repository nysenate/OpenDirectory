<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
%>
<html>
	<head>
		<title>Search Page</title>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<h> Search Results:</h>
					<% if (results != null) { %>
						<% for( Person result : results ) { %>
						<div id="name_pic"></div>
						<div id="search_info">
							<%=result.getFullName()%>
						</div>
						<% } %>
					<% } else { %>
						No query supplied 
					<% } %>
			</div>
	</body>
</html>