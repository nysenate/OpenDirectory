<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList,gov.nysenate.opendirectory.servlets.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%! @SuppressWarnings("unchecked") %><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
	String search_query = (String)request.getAttribute("query");
%>
<html>
	<head>
		<title>Search Page</title>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<h2> Search Results for:</h2> <h2 id="query"> <%= search_query %> </h2><br></br>
					<% if (results != null) { %>
						<% for( Person result : results ) { %>
						<div id="name_pic">
							<img src="<%=urls.url("img","einstein.jpg")%>" width="100" height="100"/>
							<a href="<%=urls.url("person",result.getUid())%>"> <%=result.getFullName()%></a>
						</div>
						<div id="search_info">
							<p><%=result.getTitle() %> , <%=result.getLocation() %> - <%=result.getDepartment() %>  </p>
						</div>
						<% } %>
					<% } else { %>
						No query supplied 
					<% } %>
			</div>
		</div>
	</body>
</html>