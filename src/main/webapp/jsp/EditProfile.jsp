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
			<div id="tabs">
				<div id="profile_tab" style="border-bottom:1px solid white;border-right:0px;"><a href="<%= urls.url("user","edit","profile")%>">Profile</a></div>
				<div id="profile_tab"><a href="<%= urls.url("user","edit","settings")%>">Settings</a></div>
			</div>
			
			<div id="main">
				<form action="" ENCTYPE='multipart/form-data' method="POST">
					<div id="edit_pic">
						<% if(user.getPicture()!=null && !user.getPicture().isEmpty()) { %>
							<img src="<%=user.getPicture()%>" width="165" height="213">
						<% } else { %>
							<img src="<%=urls.url("img","defaults","Gravatar-30.png")%>" width="165" height="213">
						<% } %>
						<br/><br/>
						<b>Change Profile Picture</b>: <input type="file" name="avatar" /><br/>
						<i>Images will be shrunk to 165x213 ratio on display.<br/>Try to keep the image file size small. </i>
					</div>
					<div id="top_info" class="right">
				
						<div id="name">
							<b><%=user.getFullName()%></b>
						</div>
						
						<div id="info">
							<% 	String lineone = "";
								if(user.getTitle()!=null && !user.getTitle().isEmpty())
									lineone+=user.getTitle();
								if(user.getLocation()!=null && !user.getLocation().isEmpty()) {
									if(!lineone.isEmpty())
										lineone+=", ";
									lineone+=user.getLocation();
								}
								if(user.getDepartment()!=null && !user.getDepartment().isEmpty()) {
									if(!lineone.isEmpty())
										lineone+=" - ";
									lineone+=user.getDepartment();
								}
								lineone =  "<p>"+lineone+"</p>";
								out.println(lineone);
								
								String linetwo = "";
								if(user.getPhone()!=null && !user.getPhone().isEmpty())
									linetwo+=user.getPhone();

								linetwo = "<p>"+linetwo+"</p>";
								out.println(linetwo);
								
								String linethree = "";
								if(user.getEmail()!=null && !user.getEmail().isEmpty())
									linethree+=user.getEmail();

								linethree = "<p>"+linethree+"</p>";
								out.println(linethree);
								
								%>
							</div>
						</div>
								
					<!--  <div id="edit_contactinfo">
						<br/><br/>
						<p>Contact Information</p>
						<b>E-mail</b>: <%=user.getEmail()%>
						<br/><br/>
						<b>Phone</b>: <%=user.getPhone()%>
						<br/><br/>
						<b>Department</b>: <%=user.getDepartment()%>
						<br/><br/>
						<b>Title</b>: <%=user.getTitle()%>
						<br/><br/>
						<b>Location</b>: <%=user.getLocation()%>
						<br/><br/>
					</div>-->
					<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
					<div id="edit_addinfo">
						<p>Additional Contact Information</p>
						<b>E-mail</b> <input type="text" value="<%=email2%>" name="email2">
						<br></br>
						<b>Phone (###) ###-####</b> <input type="text" value="<%=phone2%>" name="phone2"></input>
						<br></br>
						<b>IRC Alias</b> <input type="text" value="<%=irc%>" name="irc"></input>
					</div>
					<br/>
					<div id="edit_generalinfo">
						<p>General Information</p>
						<div id="edit_bio">
							<b>Biography</b>
							<br></br>
							<textarea name="bio" cols="100" rows="5"><%=user.getUnprocessedBio()%></textarea><br></br>
						</div>
						<div id="edit_skills">
							<br></br>
							<b>Skills</b>
							<p>**Separate Skills With a Comma or Semicolon</p>
							<textarea name="skills" cols="100" rows="2"><%=user.getSkills().toString().substring(1,user.getSkills().toString().length()-1) %></textarea><br>
						</div>
						<div id="edit_interests">
							<br></br>
							<b>Interests</b>
							<p>**Separate Interests With a Comma or Semicolon</p>
							<textarea name="interests" cols="100" rows="2"><%=user.getInterests().toString().substring(1,user.getInterests().toString().length()-1) %></textarea><br>
						</div>
					</div>
					<br/>
					<div id="edit_socialinfo">
						<p>Social Links</p>
						<b>LinkedIn (Profile URL)</b> <input type="text" value="<%=linkedin%>" name="linkedin"></input>
						<br></br>
						<b>FaceBook (Profile URL)</b> <input type="text" value="<%=facebook%>" name="facebook"></input>
						<br></br>
						<b>Twitter (Profile URL)</b> <input type="text" value="<%=twitter%>" name="twitter"></input>
						<br></br>
					</div>
					<div id="submit_changes_Button" align="center">
						<br></br>
						<button name="submit_changes" type="submit">Submit Changes</button>
					</div>
				</form>
			</div>
<jsp:include page="footer.jsp" />