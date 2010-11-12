<%@ page language="java" import="gov.nysenate.opendirectory.models.Person" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	Person person = (Person)request.getAttribute("person");
%><!DOCTYPE html5>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><%=person.getFullName() %></title>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<div id="pic">
					<img src="/opendirectory/img/einstein.jpg" width="100" height="100">
				</div>
				<span id="top_info" class="right">
	
					<div id="name">
						<b><%=person.getFullName()%></b>
					</div>
					<div id="info">
						<p><%=person.getTitle() %> , <%=person.getLocation() %> - <%=person.getDepartment() %>  </p>
						<p><%=person.getPhone() %>, <a href="mailto:<%=person.getEmail()%>"><%=person.getEmail() %></a> </p>
						<br></br>
					</div>
				</span>
				<div id="bio">
					<b>Bio</b>
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