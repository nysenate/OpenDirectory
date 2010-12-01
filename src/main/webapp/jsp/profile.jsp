<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.servlets.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person person = (Person)request.getAttribute("person");
	Person user = (Person)request.getAttribute("user");
%><!DOCTYPE html5>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=person.getFullName() %></title>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<div id="pic">
					<img src="<%=urls.url("img","einstein.jpg")%>" width="150" height="200">
				</div>
				<div id="top_info" class="right">
				
					<div id="name">
						<b><%=person.getFullName()%></b>
					</div>
					<div id="info">
						<p><%=person.getTitle() %> , <%=person.getLocation() %> - <%=person.getDepartment() %>  </p>
						<p><%=person.getPhone() %>, <a href="mailto:<%=person.getEmail()%>"><%=person.getEmail() %></a> </p>
						<a href="<%=urls.url("user","addbookmark",person.getUid())%>"> Add Me to Your Bookmarks!</a>
						<br></br>
					</div>
				</div>
				<br></br>
				<div id="bio">
					<% if(person.getBio()!=null && !person.getBio().isEmpty()) { %>
						<b>Biography</b>
						<p><%=person.getBio() %></p>
					<% } %>
				</div>
			</div>
			<% if(person.getInterests()!=null && !person.getInterests().isEmpty()) { %>
				<div id="interests">
					<b>Interests</b>
					<p><%=person.getInterests().toString().substring(1,person.getInterests().toString().length()-1) %></p>
				</div>
			<% } %>
			<% if(person.getSkills()!=null && !person.getSkills().isEmpty()) { %>
				<div id="skills">
					<b>Skills</b>
					<p><%=person.getSkills().toString().substring(1,person.getSkills().toString().length()-1) %></p>
				</div>
			<% } %>
			<div id="add_info">
				<% if(person.getInterests()!=null) { %>
					<b>Additional Information</b>
					<p>Follow me on <a href="<%= person.getTwitter() %>"> Twitter </a> and <a href="<%= person.getFacebook() %>"> Facebook </a></p>
				<% } %>
			</div>
		</div>
	</body>
</html>