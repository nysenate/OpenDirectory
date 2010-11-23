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
						<b>Biography</b> <input type="radio" name="bio" value="bio_pub"> Public </input> <input type="radio" name="bio" value="bio_sen"> Senate </input> <input type="radio" name="bio" value="bio_priv"> Private </input>
						<br></br>
						<form method="post" action="">
						<textarea name="bio_content" cols="100" rows="5">
						</textarea><br></br>
					</div>
						<div id="edit_skills">
							<b>Skills</b> <input type="radio" name="skills" value="skills_pub"> Public </input> <input type="radio" name="skills" value="skills_sen"> Senate </input> <input type="radio" name="skills" value="skills_priv"> Private </input>
							<p>**Seperate Skills With a Comma or Semicolon</p>
							<form method="post" action="">
							<textarea name="skills_content" cols="100" rows="5">
							</textarea><br>
						</div>
							<div id="edit_interests">
								<br></br>
								<b>Interests</b> <input type="radio" name="int" value="int_pub"> Public </input> <input type="radio" name="int" value="int_sen"> Senate </input> <input type="radio" name="int" value="int_priv"> Private </input>
								<p>**Seperate Interests With a Comma or Semicolon</p>
								<form method="post" action="">
								<textarea name="interests_content" cols="100" rows="5">
								</textarea><br>
							</div>
								<div id="edit_addinfo">
									<p>Additional Contact Information</p>
									<b>E-mail</b> <input type="text"></input><input type="radio" name="email" value="email_pub"> Public </input> <input type="radio" name="email" value="email_sen"> Senate </input> <input type="radio" name="email" value="email_priv"> Private </input>
									<br></br>
									<b>Phone</b> <input type="text"></input><input type="radio" name="phone" value="phone_pub"> Public </input> <input type="radio" name="phone" value="phone_sen"> Senate </input> <input type="radio" name="phone" value="phone_priv"> Private </input>
									<br></br>
									<b>IRC Alias</b> <input type="text"></input><input type="radio" name="irc" value="irc_pub"> Public </input> <input type="radio" name="irc" value="irc_sen"> Senate </input> <input type="radio" name="irc" value="irc_priv"> Private </input>
									<br></br>
									<b>LinkedIn</b> <input type="text"></input><input type="radio" name="link" value="link_pub"> Public </input> <input type="radio" name="link" value="link_sen"> Senate </input> <input type="radio" name="link" value="link_priv"> Private </input>
									<br></br>
									<b>FaceBook</b> <input type="text"></input><input type="radio" name="face" value="face_pub"> Public </input> <input type="radio" name="face" value="face_sen"> Senate </input> <input type="radio" name="face" value="face_priv"> Private </input>
									<br></br>
									<b>Twitter</b> <input type="text"></input><input type="radio" name="twit" value="twit_pub"> Public </input> <input type="radio" name="twit" value="twit_sen"> Senate </input> <input type="radio" name="twit" value="twit_priv"> Private </input>
									<br></br>
								</div>
									<div id="submit_changes_Button" align="center">
										<br></br>
										<button name="submit_changes" type="submit">Submit Changes</button>
										</div>
			</div>
	</body>
</html>