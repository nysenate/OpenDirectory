<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person user = (Person)request.getAttribute("user");
%>
<div id="header">
	<a href="<%=urls.url("index")%>">
		<img src="http://www.nysenate.gov/sites/all/themes/nys/images/nyss_logo.png" id="logo" />
	</a>

	<div id="user_box">
		<% if (user == null) { %>
			<ul id="user_top_bar">
				<li><a href="<%=urls.url("user","login")%>">LOGIN</a></li>
				<li><a href="<%=urls.url("index")%>">HOME</a></li>
			</ul>
		<% } else { %>
			<ul id="user_top_bar">
				<li>Welcome, <%=user.getFirstName()%></li>
				<li><a href="<%=urls.url("user","logout")%>">LOGOUT</a></li>
				<li><a href="<%=urls.url("index")%>">HOME</a></li>
			</ul>
			
			<ul id="user_bottom_bar">
				<li><a href="<%=urls.url("person",user.getUid())%>">My Profile</a></li> 
				<li><a href="<%=urls.url("user","bookmarks")%>">My Bookmarks</a></li> 
				<li><a href="<%=urls.url("user","edit")%>">Edit Profile</a></li>
			</ul>
		<% } %>
	</div>
</div>

<div id="nav_bar">
	<form id="nav_search" action="<%=urls.url("search")%>" method="GET">
		Search:<input type="text" name="query" size="20" maxlength="255" value="" id="nav_search_input" />
		<input type="submit" value = "Search" id="nav_search_button"></input>
	</form>
	Browse by:
	<ul>
		<li><a href="<%=urls.url("browse","lastname")%>" class="nav">Last name</a><div class="sep">|</div></li>
		<li><a href="<%=urls.url("browse","firstname")%>" class="nav">First name</a><div class="sep">|</div></li>
		<li><a href="<%=urls.url("browse","department")%>" class="nav">Department</a><div class="sep">|</div></li>
		<li><a href="<%=urls.url("browse","location")%>" class="nav">Location</a></li>
	</ul>
</div>