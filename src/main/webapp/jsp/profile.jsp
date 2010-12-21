<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person person = (Person)request.getAttribute("person");
	Person user = (Person)request.getAttribute("user");
	
%><jsp:include page="header.jsp" />
			<% if( user!=null && person.getUid().equals(user.getUid())) { %>
				<div id="edit_button">
					<a id="edit_link" href="<%=urls.url("user","edit")%>">Edit your profile</a>
				</div>
			<% } %>
			<div id="main">
				<div id="pic">
					<% if(person.getPicture()!=null && !person.getPicture().isEmpty()) { %>
						<img src="<%=person.getPicture()%>" width="165" height="213">
					<% } else { %>
						<img src="<%=urls.url("img","defaults","Gravatar-30.png")%>" width="165" height="213" />
					<% } %>
				</div>
				<div id="top_info" class="right">
				
					<div id="name">
						<b><%=person.getFullName()%></b>
					</div>
					
					<div id="info">
						<% 	String lineone = "";
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
							lineone =  "<p>"+lineone+"</p>";
							out.println(lineone);
							
							String linetwo = "";
							if(person.getPhone()!=null && !person.getPhone().isEmpty())
								linetwo+=person.getPhone();
							if(person.getPhone2()!=null && !person.getPhone2().isEmpty()) {
								if(!linetwo.isEmpty())
									linetwo+=" / ";
								linetwo+=person.getPhone2();
							}
							linetwo = "<p>"+linetwo+"</p>";
							out.println(linetwo);
							
							String linethree = "";
							if(person.getEmail()!=null && !person.getEmail().isEmpty())
								linethree+=person.getEmail();
							if(person.getEmail2()!=null && !person.getEmail2().isEmpty()) {
								if(!linetwo.isEmpty())
									linethree+=" / ";
								linethree+=person.getEmail2();
							}
							linethree = "<p>"+linethree+"</p>";
							out.println(linethree);
						
						if(user!=null && user.getUid()!=null && !user.getUid().equals(person.getUid())) { 
								if(!user.getBookmarks().contains(person)) {
									%><a href="<%=urls.url("user","bookmarks","add", person.getUid())%>"> Add me to your bookmarks!</a> <br></br><%
								} else {
									%><a href="<%=urls.url("user","bookmarks","remove", person.getUid())%>"> Remove me from your bookmarks!</a><br/><br/><%
								}
						} %>
						<a href="<%=urls.url("person", person.getUid(), "vcard")%>"> Download VCard </a>
						<p>
						
							<% if(person.getFacebook()!= null && !person.getFacebook().isEmpty()) {  %>
								<a href="<%=person.getFacebook() %>" class="social_link">
									<img src="<%=urls.url("img","facebook16x16.png")%>" width="16" height="16" />
								</a>
							<% } %>
							
							<% if(person.getTwitter()!= null && !person.getTwitter().isEmpty()) {  %>
								<a href="<%=person.getTwitter() %>" class="social_link">
									<img src="<%=urls.url("img","twitter16x16.png")%>" width="16" height="16" />
								</a>
							<% } %>
							
							<% if(person.getLinkedin()!= null && !person.getLinkedin().isEmpty()) {  %>
								<a href="<%=person.getLinkedin() %>" class="social_link">
									<img src="<%=urls.url("img","linkedin16x16.png")%>" width="16" height="16" />
								</a>
							<% } %>
						
						</p>
					</div>
					<div class="clear"></div>
				</div>				
		</div>			
			<div id="interests">
			<% if(person.getInterests()!=null && !person.getInterests().isEmpty()) { %>
				
					<b>Interests</b>
					<p><%
						String last = person.getInterests().last();
						for( String interest : person.getInterests() ) {
							out.print("<a href=\""+urls.url("search","?query=interests:("+interest+")")+"\">"+interest+"</a>");
							if(!interest.equals(last))
								out.println(", ");
						}
					%></p>
					
			<% } else { %>
					<b>Interests</b>
					<p>Information Not Available</p>
			<% } %>
			</div>
			
			<div id="skills">
			<% if(person.getSkills()!=null && !person.getSkills().isEmpty()) { %>
				
					<b>Skills</b>
					<p><%
						String last = person.getSkills().last();
						for( String skill : person.getSkills() ) {
							out.print("<a href=\""+urls.url("search","?query=skills:("+skill+")")+"\">"+skill+"</a>");
							if(!skill.equals(last))
								out.println(", ");
						}
					%></p>
					
			<% } else { %>
					<b>Skills</b>
					<p>Information Not Available</p>
				
			<% } %>
			</div>
			<div id="main">
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
			
		</div>
<jsp:include page="footer.jsp"/>