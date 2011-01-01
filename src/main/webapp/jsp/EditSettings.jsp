<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper,java.util.TreeSet,java.util.HashMap" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	Person user = (Person)request.getAttribute("user");	
	String message = (String)request.getAttribute("message");	
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<String>> permissions = user.getPermissions();
	String error = (String)request.getAttribute("error");	
%><%!

public String writeRadio(String name, String type, boolean checked) {
	return "<input type=\"radio\" name=\"radio_"+name+"\" value=\""+type+"\" "+(checked ? "checked" : "")+">"+type+"</input>";
}

public String writeRadios(String internal_name, String cred) {
	String html = "";
	html += writeRadio(internal_name,"Public",cred.equals("public"));
	html += writeRadio(internal_name,"Senate",cred.equals("senate"));
	html += writeRadio(internal_name,"Private",cred.equals("private"));
	return html;
}

%><jsp:include page="header.jsp" />
			
			
			<% if(message!=null) { %>
				<div id="edit_button">
					<b><%=message%></b>
				</div>
			<% } %>
			<div id="edit_error" style="font-size:85%; width:60%; display:none;"><%= error!=null ? "It looks like there may be a problem with one of your contact<br/>fields.  Please check to make sure you:<br/>" + error:"" %></div>
			
			<div id="main">
					<div class="ratio_left" id="main_left">
						<div class="image">
							<% if(user.getPicture()!=null && !user.getPicture().isEmpty()) { %>
								<img src="<%=user.getPicture()%>" width="165" height="213"><br/>
								<i style="font-size:60%;"><a href="<%=urls.url("user","edit","deletePicture") %>">(delete your image)</a></i>
							<% } else { %>
								<img src="<%=urls.url("img","defaults","Gravatar-30.png")%>" width="165" height="213" />
							<% } %>
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
							<div style="text-align:center;text-decoration:bold;">
								<a href="<%= urls.url("user","edit","profile")%>">Profile</a><div class="sep">|</div>
								Settings
							</div>
							
							
							
							<form action="" method="POST">
								<div id="edit_form_field">
									<br/>
									<b>Would you like your profile to be displayed in the Recently Updated section on the home page?</b>
									<select name="frontPage">
										<option value="true" <%=user.getFrontPage() ? "SELECTED":""%>>Yes</option>
										<option value="false" <%=!user.getFrontPage() ? "SELECTED":""%>>No</option>
									</select>
									
									<p>Contact Information</p>
									<ol>
										<li>
											<label id="edit_input"><b>E-mail</b>:</label>
											<%= writeRadios("email",user.getLowestPermission(permissions.get("email"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Phone</b>:</label>
											<%= writeRadios("phone",user.getLowestPermission(permissions.get("phone"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Department</b>:</label>
											<%= writeRadios("department",user.getLowestPermission(permissions.get("department"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Title</b>:</label>
											<%= writeRadios("title",user.getLowestPermission(permissions.get("title"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Location</b>:</label>
											<%= writeRadios("location",user.getLowestPermission(permissions.get("location"))) %>
										</li>
									</ol>
									<br/>
									<p>Additional Contact Information</p>
									<ol>
										<li>
											<label id="edit_input"><b>E-mail</b>:</label>
											<%= writeRadios("email2",user.getLowestPermission(permissions.get("email2"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Phone</b>:</label>
											<%= writeRadios("phone2",user.getLowestPermission(permissions.get("phone2"))) %>
										</li>
										<li>
											<label id="edit_input"><b>IRC Alias</b>:</label>
											<%= writeRadios("irc",user.getLowestPermission(permissions.get("irc"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Linkedin</b>:</label>
											<%= writeRadios("linkedin",user.getLowestPermission(permissions.get("linkedin"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Facebook</b>:</label>
											<%= writeRadios("facebook",user.getLowestPermission(permissions.get("facebook"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Twitter</b>:</label>
											<%= writeRadios("twitter",user.getLowestPermission(permissions.get("twitter"))) %>
										</li>
									</ol>
									<br/>
									<p>General Information</p>
									<ol>
										<li>
											<label id="edit_input"><b>About Me</b>:</label>
											<%= writeRadios("bio",user.getLowestPermission(permissions.get("bio"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Skills</b>:</label>
											<%= writeRadios("skills",user.getLowestPermission(permissions.get("skills"))) %>
										</li>
										<li>
											<label id="edit_input"><b>Interests</b>:</label>
											<%= writeRadios("interests",user.getLowestPermission(permissions.get("interests"))) %>
										</li>
									</ol>
									<div id="submit_changes_Button" align="center">
										<br>
										<button name="submit_changes" type="submit">Submit Changes</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
<jsp:include page="footer.jsp" />