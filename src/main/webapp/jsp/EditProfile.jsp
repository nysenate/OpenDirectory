<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.servlets.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person person = (Person)request.getAttribute("person");
%><!DOCTYPE html5>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title> Edit Profile </title>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<div id="edit_pic">
					<img src="<%=urls.url("img","einstein.jpg")%>" width="200" height="200">
					<br></br>
					<a href=""> Change Profile Picture </a>
					<br></br>
				</div>
					<div id="edit_bio">
						<br></br>
						<b>Biography</b> <input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
						<br></br>
						<form method="post" action="">
						<textarea name="bio_content" cols="100" rows="5">
						</textarea><br></br>
					</div>
						<div id="edit_skills">
							<b>Skills</b> <input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
							<p>**Seperate Skills With a Comma or Semicolon</p>
							<form method="post" action="">
							<textarea name="skills_content" cols="100" rows="5">
							</textarea><br>
						</div>
							<div id="edit_interests">
								<br></br>
								<b>Interests</b> <input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
								<p>**Seperate Interests With a Comma or Semicolon</p>
								<form method="post" action="">
								<textarea name="interests_content" cols="100" rows="5">
								</textarea><br>
							</div>
								<div id="edit_addinfo">
									<p>Additional Contact Information</p>
									<b>E-mail</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
									<b>Phone</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
									<b>IRC Alias</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
									<b>LinkedIn</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
									<b>FaceBook</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
									<b>Twitter</b> <input type="text"></input><input type="checkbox"> Public </input> <input type="checkbox"> Senate </input> <input type="checkbox"> Private </input>
									<br></br>
								</div>
									<div id="submit_changes_Button" align="center">
										<br></br>
										<button name="submit_changes" type="submit">Submit Changes</button>
										</div>
			</div>
	</body>
</html>