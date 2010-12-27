<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper,java.util.TreeSet,java.util.HashMap" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	Person user = (Person)request.getAttribute("user");	
	String message = (String)request.getAttribute("message");	
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<String>> permissions = user.getPermissions();
	String error = (String)request.getAttribute("error");
	
	String phone2 = (String)request.getAttribute("phone2");
	String email2 = (String)request.getAttribute("email2");
	String irc = (String)request.getAttribute("irc");
	String twitter = (String)request.getAttribute("twitter");
	String facebook = (String)request.getAttribute("facebook");
	String linkedin = (String)request.getAttribute("linkedin");
	
	phone2 = (phone2 == null) ? user.getPhone2():phone2;
	email2 = (email2 == null) ? user.getEmail2():email2;
	irc = (irc == null) ? user.getIrc():irc;
	twitter = (twitter == null) ? user.getTwitter():twitter;
	facebook = (facebook == null) ? user.getFacebook():facebook;
	linkedin = (linkedin == null) ? user.getLinkedin():linkedin;
	
%><%!


%><jsp:include page="header.jsp" />
			<% if(message!=null) { %>
				<div id="edit_button">
					<b><%=message%></b>
				</div>
			<% } %>
			<div id="edit_error" style="font-size:85%; width:60%; display:none;"><%= error!=null ? "It looks like there may be a problem with one of your contact<br/>fields.  Please check to make sure you:<br/>" + error:"" %></div>
			
			<div id="main">
				<form action="" ENCTYPE='multipart/form-data' method="POST">
					<div class="ratio_left" id="main_left">
						<div class="image">
							<% if(user.getPicture()!=null && !user.getPicture().isEmpty()) { %>
								<img src="<%=user.getPicture()%>" width="165" height="213"><br/>
								<i style="font-size:60%;"><a href="<%=urls.url("user","edit","deletePicture") %>">(delete your image)</a></i>
							<% } else { %>
								<img src="<%=urls.url("img","defaults","Gravatar-30.png")%>" width="165" height="213" />
							<% } %>
							<br/><b style="font-size:80%;">Choose Picture</b> <input type="file" name="avatar" /><br/>
							<i style="font-size:60%;">Images may be altered to fit a 164x213 display.<br/>Please keep image file sizes under 300kb.</i>
						</div>
						
						<div class="admin">
							<% if( user!=null && user.getUid().equals(user.getUid())) { %>
									<p id="profile_edit"><a href="<%=urls.url("person",user.getUid(),"profile")%>">Back to your profile</a></p>
							<% } %>
						</div>
						<div class="contact">
							<%if(user.getLocation()!=null && !user.getLocation().isEmpty()) {%>
								  <p id="senate_location"><%=user.getLocation() %></p>
							<%} %>
							<%if(user.getPhone()!=null && !user.getPhone().isEmpty()) { %>
								<p id="senate_phone"><%= user.getPhone()%></p>
							<% } %>
	
							<%if(user.getEmail()!=null && !user.getEmail().isEmpty()) { %>
								<p id="senate_email"><a href="mailto:<%= user.getEmail()%>"><%= user.getEmail() %></a></p>
							<% } %>
						</div>
					</div>			
				
					<div class="ratio_right" id="main_right">
						<div id="header_person">
							<% if(user.getFullName() != null && !user.getFullName().isEmpty()) { %>
							<h3 id="name"><%=user.getFullName() %></h3>
							<% } %>
							<% if(user.getTitle() != null && !user.getTitle().isEmpty()) { %>
								<h3 id="title"><%=user.getTitle() %></h3>
							<% } %>
							<% if(user.getDepartment() != null && !user.getDepartment().isEmpty()) { %>
								<h3 id="office"><%=user.getDepartment() %></h3>
							<% } %>
						</div>
						<div id="edit_container">
							<div style="text-align:center;font-size:80%;">
								Profile<div class="sep">|</div>
								<a href="<%= urls.url("user","edit","settings")%>">Settings</a>
							</div>
							<p>Additional Contact Information</p>
								<div id="edit_form_field" style="float:left;">
									<ol>
										<li>
											<label id="edit_input"><b>E-mail</b></label>
											<input type="text" value="<%=email2%>" name="email2">
										</li>
										<li>
											<label id="edit_input"><b>Phone</b></label>
											<input type="text" value="<%=phone2.equals("") ? "(###) ###-####":phone2%>" name="phone2"></input>
										</li>
										<li>
											<label id="edit_input"><b>IRC Alias</b></label>
											<input type="text" value="<%=irc%>" name="irc"></input>
										</li>
									</ol>
									<!-- Phone  -->
								</div>
								<div id="edit_form_field" style="float:left;">
									<ol>
										<li>
											<label id="edit_input"><b>LinkedIn</b></label>
											<input type="text" value="<%=linkedin%>" name="linkedin"></input>
										</li>
										<li>
											<label id="edit_input"><b>Faceook</b></label>
											<input type="text" value="<%=facebook%>" name="facebook"></input>
										</li>
										<li>
											<label id="edit_input"><b>Twitter</b></label>
											<input type="text" value="<%=twitter%>" name="twitter"></input>
										</li>
									</ol>
									 <!--  (Profile URL) -->
								</div>
							<div id="" style="margin-top:140px;">
								<div id="">
									<b>About Me</b>
									<br/>
									<textarea name="bio" style="width:95%;" rows="5"><%=user.getUnprocessedBio()%></textarea>
								</div>
								<div id="">
									<b>Skills</b>
									<br/>
									<i style="font-size:70%;">Separate Skills With a Comma or Semicolon</i>
									<textarea name="skills" style="width:95%;" rows="4"><%=user.getSkills().toString().substring(1,user.getSkills().toString().length()-1) %></textarea>
								</div>
								<div id="">
									<b>Interests</b>
									<br/>
									<i style="font-size:70%;">Separate Interests With a Comma or Semicolon</i>
									<textarea name="interests" style="width:95%;" rows="4"><%=user.getInterests().toString().substring(1,user.getInterests().toString().length()-1) %></textarea>
								</div>
							</div><br/>
							<div id="submit_changes_Button" align="center">
								<button name="submit_changes" type="submit">Submit Changes</button>
							</div>
						</div>
					</div>
				</form>
			</div>
<jsp:include page="footer.jsp" />