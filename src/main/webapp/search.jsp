<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
	String search_query = (String)request.getAttribute("query");
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
				<h> Search Results for:</h> <h id="query"> <%= search_query %> </h><br></br>
					<% if (results != null) { %>
						<% for( Person result : results ) { %>
						<div id="name_pic">
							<img src="/opendirectory/img/einstein.jpg" width="50" height="50"/> <a href="/opendirectory/person/<%=result.getUid()%>"> <%=result.getFullName()%> </a>
						</div>
						<div id="search_info">
							<p><%=result.getTitle() %> , <%=result.getLocation() %> - <%=result.getDepartment() %>  </p>
						</div>
						<% } %>
					<% } else { %>
						No query supplied 
					<% } %>
			</div>
	</body>
</html>