<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person person = (Person)request.getAttribute("person");
	Person user = (Person)request.getAttribute("user");
	
%><jsp:include page="header.jsp" />
			
			<div id="main">
			
				<div class="ratio_left" id="main_left">
					<div class="image">
						<% if(person.getPicture()!=null && !person.getPicture().isEmpty()) { %>
							<img src="/uploads/avatars/profile/<%=person.getPicture()%>" width="165" height="213">
						<% } else { %>
							<img src="<%=urls.url("img","default_gravatar.png")%>" width="165" height="213" />
						<% } %>
					</div>
					
					<div class="admin">
						<% if( user!=null && person.getUid().equals(user.getUid())) { %>
								<p id="profile_edit"><a href="<%=urls.url("user","edit")%>">Edit Profile</a></p>
						<% } %>
						
						<% if(user!=null && user.getUid()!=null && !user.getUid().equals(person.getUid())) { 
								if(!user.getBookmarks().contains(person)) {
									%><p id="profile_bookmark"><a href="<%=urls.url("user","bookmarks","add", person.getUid())%>">Bookmark Me</a></p><%
								} else {
									%><p id="profile_bookmark"><a href="<%=urls.url("user","bookmarks","remove", person.getUid())%>">Remove Bookmark</a></p><%
								}
						} %>
					</div>
					<div class="contact">
						<%if(person.getLocation()!=null && !person.getLocation().isEmpty()) {%>
							  <p id="senate_location"><a href="<%=urls.url("search","?query=location:("+person.getLocation().replaceAll(" ","+")+")") %>"><%=person.getLocation() %></a></p>
						<%} %>
						<%if(person.getPhone()!=null && !person.getPhone().isEmpty()) { %>
							<p id="senate_phone"><%= person.getPhone()%></p>
						<% } %>
						<%if(person.getPhone2()!=null && !person.getPhone2().isEmpty()) { %>
							<p id="additional_phone"><%= person.getPhone2()%></p>
						<% } %>
						<%if(person.getEmail()!=null && !person.getEmail().isEmpty()) { %>
							<p id="senate_email"><a href="mailto:<%= person.getEmail()%>"><%= person.getEmail() %></a></p>
						<% } %>
						<%if(person.getEmail2()!=null && !person.getEmail2().isEmpty()) { %>
							<p id="additional_email"><a href="mailto:<%= person.getEmail2() %>"><%= person.getEmail2() %></a></p>
						<% } %>
						<%if(person.getIrc() != null && !person.getIrc().isEmpty()) { %>
							<p id="additional_irc"><%=person.getIrc() %></p>
						<% } %>  
					</div>
					<div class="social">
					
						<% if(person.getTwitter()!= null && !person.getTwitter().isEmpty()) {  %>
							<a href="<%=person.getTwitter() %>" class="social_link">
								<img src="<%=urls.url("img","social_twitter_24x24.png")%>">
							</a>
						<% } %>
						<% if(person.getLinkedin()!= null && !person.getLinkedin().isEmpty()) {  %>
							<a href="<%=person.getLinkedin() %>" class="social_link">
								<img src="<%=urls.url("img","social_linkedin_24x24.png")%>">
							</a>
						<% } %>
						<% if(person.getFacebook()!= null && !person.getFacebook().isEmpty()) {  %>
							<a href="<%=person.getFacebook() %>" class="social_link">
								<img src="<%=urls.url("img","social_facebook_24x24.png")%>">
							</a>
						<% } %>
					</div>
					<div class="vcard">
						<a href="<%=urls.url("person", person.getUid(), "vcard")%>"> Download vCard </a>
					</div>
					<div id="skills">
						<h3>Skills</h3>
   						<ul>
							<% if(person.getSkills()!=null && !person.getSkills().isEmpty()) {
								String last = person.getSkills().last();
								for(String skill : person.getSkills() ) {
									%><li><a href="<%=urls.url("search","?query=skills:("+skill+")") %>"><%=skill %></a></li><%
								}
							%>
							<% } else { %>
									<li>Information Not Available</li>
								
							<% } %>
						</ul>
					</div>
					<div id="interests">
						<h3>Interests</h3>
   						<ul>
							<% if(person.getInterests()!=null && !person.getInterests().isEmpty()) {
								String last = person.getInterests().last();
								for(String interest : person.getInterests() ) {
									%><li><a href="<%=urls.url("search","?query=interests:("+interest+")")%>"><%=interest%></a></li> <%
								}
							%>
							<% } else { %>
									<li>Information Not Available</li>
								
							<% } %>
						</ul>
					</div>
				</div>
				<div class="ratio_right" id="main_right">
					<div id="header_person">
							<% if(person.getFullName() != null && !person.getFullName().isEmpty()) { %>
							<h3 id="name"><%=person.getFullName() %></h3>
							<% } else { %> <br/> <% } %>
							<% if(person.getTitle() != null && !person.getTitle().isEmpty()) { %>
								<h3 id="title"><a href="<%=urls.url("search","?query=title:("+person.getTitle().replaceAll(" ","+")+")") %>"><%=person.getTitle() %></a></h3>
							<% } else { %> <br/> <% } %>
							<% if(person.getDepartment() != null && !person.getDepartment().isEmpty()) { %>
								<h3 id="office"><a href="<%=urls.url("search","?query=department:("+person.getDepartment().replaceAll(" ","+")+")") %>"><%=person.getDepartment() %></a></h3>
							<% } else { %> <br/><br/> <% } %>
					</div>
					<div id="bio">
						<% if(person.getBio() != null && !person.getBio().isEmpty()) { %>
							<%=person.getBio() %>
						<% } else { %>
							<%="<p>About Me not available</p>" %>
						<% } %>
					</div>
					
				</div>
			</div>
<jsp:include page="footer.jsp"/>