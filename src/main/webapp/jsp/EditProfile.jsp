<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.servlets.utils.UrlMapper,java.util.TreeSet,java.util.HashMap" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person user = (Person)request.getAttribute("user");
	String message = (String)request.getAttribute("message");
	HashMap<String,TreeSet<String>> permissions = user.getPermissions();
%><%!

public String writeRadio(String name, String type, boolean checked) {
	return "<input type=\"radio\" name=\"radio_"+name+"\" value=\""+type+"\" "+(checked ? "checked" : "")+">"+type+"</input>";
}

public String writeRadios(String internal_name, TreeSet<String> defaults) {
	String html = "";
	html += writeRadio(internal_name,"Public",defaults.contains("public"));
	html += writeRadio(internal_name,"Senate",defaults.contains("senate"));
	html += writeRadio(internal_name,"Private",defaults.contains("private"));
	return html;
}

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
				<% if(message!=null) { %>
					<b><%=message%></b>
				<% } %>
				<form action="" method="POST">
					<div id="edit_pic">
						<img src="<%=urls.url("img","einstein.jpg")%>" width="200" height="200">
						<br/><br/>
						<a href=""> Change Profile Picture </a>
					</div>
					<br/>
					<div id="edit_contactinfo">
						<br/><br/>
						<p>Contact Information</p>
						<b>E-mail</b>: <%=user.getEmail()%> <%= writeRadios("email",permissions.get("email")) %>
						<br/><br/>
						<b>Phone</b>: <%=user.getPhone()%> <%= writeRadios("phone",permissions.get("phone")) %>
						<br/><br/>
						<b>Department</b>: <%=user.getDepartment()%> <%= writeRadios("department",permissions.get("department")) %>
						<br/><br/>
						<b>Title</b>: <%=user.getTitle()%> <%= writeRadios("title",permissions.get("title")) %>
						<br/><br/>
						<b>Location</b>: <%=user.getLocation()%> <%= writeRadios("location",permissions.get("location")) %>
						<br/><br/>
					</div>
					<br/>
					<div id="edit_addinfo">
						<p>Additional Contact Information</p>
						<b>E-mail</b> <input type="text"><%= writeRadios("email2",permissions.get("email2")) %>
						<br></br>
						<b>Phone</b> <input type="text"></input><%= writeRadios("phone2",permissions.get("phone2")) %>
						<br></br>
						<b>IRC Alias</b> <input type="text"></input><%= writeRadios("irc",permissions.get("irc")) %>
					</div>
					<br/>
					<div id="edit_generalinfo">
						<p>General Information</p>
						<div id="edit_bio">
							<b>Biography</b>
							<%= writeRadios("bio",permissions.get("bio")) %>
							<br></br>
							<textarea name="bio" cols="100" rows="5"><%=user.getBio()%></textarea><br></br>
						</div>
						<div id="edit_skills">
							<b>Skills</b>
							<%= writeRadios("skills",permissions.get("skills")) %>
							<p>**Separate Skills With a Comma or Semicolon</p>
							<textarea name="skills" cols="100" rows="2"><%=user.getSkills().toString().substring(1,user.getSkills().toString().length()-1) %></textarea><br>
						</div>
						<div id="edit_interests">
							<br></br>
							<b>Interests</b>
							<%= writeRadios("interests",permissions.get("interests")) %>
							<p>**Separate Interests With a Comma or Semicolon</p>
							<textarea name="interests" cols="100" rows="2"><%=user.getInterests().toString().substring(1,user.getInterests().toString().length()-1) %></textarea><br>
						</div>
					</div>
					<br/>
					<div id="edit_socialinfo">
						<p>Social Links</p>
						<b>LinkedIn</b> <input type="text"></input><%= writeRadios("linkedin",permissions.get("linkedin")) %>
						<br></br>
						<b>FaceBook</b> <input type="text"></input><%= writeRadios("facebook",permissions.get("facebook")) %>
						<br></br>
						<b>Twitter</b> <input type="text"></input><%= writeRadios("twitter",permissions.get("twitter")) %>
						<br></br>
					</div>
					<div id="submit_changes_Button" align="center">
						<br></br>
						<button name="submit_changes" type="submit">Submit Changes</button>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>