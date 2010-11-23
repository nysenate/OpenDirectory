<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.servlets.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person person = (Person)request.getAttribute("person");
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
						<br></br>
					</div>
				</div>
				<br></br>
				<br></br>
				<div id="bio">
					<b>Biography</b>
					<p>I am awesome and so is the NYSS team working on OpenDirectory which will redefine the way government works </p>
				</div>
			</div>
			<div id="interests">
				<b>Interests</b>
				<p>Basketball, Baseball, Movies, Chess, Video Games, Other things</p>
			</div>
			<div id="skills">
				<b>Skills</b>
				<p>Web Development, Java, C++, PHP, C#, Python, Writing  </p>
			</div>
			<div id="add_info">
				<b>Additional Information</b>
				<p>Follow me on <a href=""> Twitter </a> and <a href=""> Facebook </a></p>
			</div>
		</div>
	</body>
</html>