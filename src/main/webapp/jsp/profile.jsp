<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
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
					<% if(person.getPicture()!=null && !person.getPicture().isEmpty()) { %>
						<img src="<%=person.getPicture()%>" width="150" height="200">
					<% } else { %>
						<img src="<%=urls.url("img","einstein.jpg")%>" width="150" height="200">
					<% } %>
				</div>
				<div id="top_info" class="right">
				
					<div id="name">
						<b><%=person.getFullName()%></b>
					</div>
					<div id="info">
						<% 	String lineone = "<p>";
							if(person.getTitle()!=null && !person.getTitle().isEmpty())
								lineone+=person.getTitle();
							if(person.getLocation()!=null && !person.getLocation().isEmpty()) {
								if(!lineone.isEmpty())
									lineone+=", ";
								lineone+=person.getLocation();
							}
							if(person.getDepartment()!=null && !person.getDepartment().isEmpty()) {
								if(!lineone.isEmpty())
									lineone+=" - ";
								lineone+=person.getDepartment();
							}
							lineone+="</p>";
							out.println(lineone);
							
							String linetwo = "<p>";
							if(person.getPhone()!=null && !person.getPhone().isEmpty())
								linetwo+=person.getPhone();
							if(person.getEmail()!=null && !person.getEmail().isEmpty()) {
								if(!linetwo.isEmpty())
									linetwo+=", ";
								linetwo+=person.getEmail();
							}
							linetwo+="</p>";
							out.println(linetwo);
						%>
						
						<a href="<%=urls.url("user","bookmarks","add", person.getUid())%>"> Add Me to Your Bookmarks!</a> <br></br>
						<a href="<%=urls.url("person", person.getUid(), "vcard.vcf")%>"> Download VCard </a>
						<br></br>
					</div>
				</div>
				<br></br>
				<br></br>
				<div id="bio">
					<% if(person.getBio()!=null && !person.getBio().isEmpty()) { %>
						<b>Biography</b>
						<p><%=person.getBio() %></p>
					<% }  else { %>
						<b>Biography</b>
						<p>Information Not Available</p>
					<% } %>
				</div>
		</div>
				
			<div id="interests">
			<% if(person.getInterests()!=null && !person.getInterests().isEmpty()) { %>
				
					<b>Interests</b>
					<p><%=person.getInterests().toString().substring(1,person.getInterests().toString().length()-1) %></p>
			<% } else { %>
					<b>Interests</b>
					<p>Information Not Available</p>
			<% } %>
			</div>
			
			<div id="skills">
			<% if(person.getSkills()!=null && !person.getSkills().isEmpty()) { %>
				
					<b>Skills</b>
					<p><%=person.getSkills().toString().substring(1,person.getSkills().toString().length()-1) %></p>
			<% } else { %>
					<b>Skills</b>
					<p>Information Not Available</p>
				
			<% } %>
			</div>
			
			<div id="add_info">
				<b>Additional Information</b>
				
				<% if(person.getEmail2()!= null && !person.getEmail2().isEmpty()) { %>
					<p>E-mail: <%=person.getEmail2() %></p>
				<% } else { %>
					<p>E-mail: Information Not Available</p>
			<% } %>
				
				<% if(person.getPhone2()!= null && !person.getPhone2().isEmpty()) { %>
					<p>Phone: <%=person.getPhone2() %></p>
				<% } else { %>
					<p>Phone: Information Not Available</p>
			<% } %>
			
				<% if(person.getIrc()!= null && !person.getIrc().isEmpty()) { %>
					<p>IRC: <%=person.getIrc() %></p>
				<% } else { %>
					<p>IRC: Information Not Available</p>
			<% } %>
			
				<% if(person.getFacebook()!= null && !person.getFacebook().isEmpty()) { %>
					<a href="<%=person.getFacebook() %>">Facebook</a>
				<% } else { %>
					<p>Facebook: Information Not Available</p>
			<% } %>
			
				<% if(person.getTwitter()!= null && !person.getTwitter().isEmpty()) { %>
					<a href="<%=person.getTwitter() %>">Twitter</a>
				<% } else { %>
					<p>Twitter: Information Not Available</p>
			<% } %>
			
				<% if(person.getLinkedin()!= null && !person.getLinkedin().isEmpty()) { %>
					<a href="<%=person.getLinkedin() %>">LinkedIn</a>
				<% } else { %>
					<p>LinkedIn: Information Not Available</p>
			<% } %>
			
			</div>
			
		</div>
	</div>
	</body>
</html>