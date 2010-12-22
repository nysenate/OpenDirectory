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

public String writeRadios(String internal_name, TreeSet<String> defaults) {
	String html = "";
	html += writeRadio(internal_name,"Public",defaults.contains("public"));
	html += writeRadio(internal_name,"Senate",defaults.contains("senate"));
	html += writeRadio(internal_name,"Private",defaults.contains("private"));
	return html;
}

%><jsp:include page="header.jsp" />
			<% if(message!=null) { %>
				<div id="edit_button">
					<b><%=message%></b>
				</div>
			<% } %>
			<div id="edit_error" style="font-size:85%; width:60%; display:none;"><%= error!=null ? "It looks like there may be a problem with one of your contact<br/>fields.  Please check to make sure you:<br/>" + error:"" %></div>
			<div id="tabs" style="align:left;">
					<div id="profile_tab"><a href="<%= urls.url("user","edit","profile")%>">Profile</a></div>
					<div id="profile_tab" style="border-bottom:1px solid white;border-left:0px;"><a href="<%= urls.url("user","edit","settings")%>">Settings</a></div>
			</div>
			<div id="main">
				<form action="" method="POST">
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
						<b>E-mail</b>: <%= writeRadios("email2",permissions.get("email2")) %>
						<br></br>
						<b>Phone</b>: <%= writeRadios("phone2",permissions.get("phone2")) %>
						<br></br>
						<b>IRC Alias</b>: <%= writeRadios("irc",permissions.get("irc")) %>
					</div>
					<br/>
					<div id="edit_generalinfo">
						<p>General Information</p>
						<b>Biography</b>: <%= writeRadios("bio",permissions.get("bio")) %>
						<br></br>
						<br></br>
						<b>Skills</b>: <%= writeRadios("skills",permissions.get("skills")) %>
						<br></br>
						<b>Interests</b>: <%= writeRadios("interests",permissions.get("interests")) %>
					</div>
					<br/>
					<div id="edit_socialinfo">
						<p>Social Links</p>
						<b>LinkedIn</b>: <%= writeRadios("linkedin",permissions.get("linkedin")) %>
						<br></br>
						<b>FaceBook</b>: <%= writeRadios("facebook",permissions.get("facebook")) %>
						<br></br>
						<b>Twitter</b>: <%= writeRadios("twitter",permissions.get("twitter")) %>
						<br></br>
					</div>
					<div id="submit_changes_Button" align="center">
						<br></br>
						<button name="submit_changes" type="submit">Submit Changes</button>
					</div>
				</form>
			</div>
<jsp:include page="footer.jsp" />