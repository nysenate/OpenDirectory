<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	Person user = (Person)request.getAttribute("user");
%>
<div id="header">
	<a href="<%=urls.url("index")%>" id="logo_link"><img src="http://www.nysenate.gov/sites/all/themes/nys/images/nyss_logo.png" id="logo" /></a>
	<div id="nav_bar">Browse by:
		<ul>
			<li><a href="<%=urls.url("browse","lastname")%>" class="nav">Last name</a><div class="sep">|</div></li>
			<li><a href="<%=urls.url("browse","firstname")%>" class="nav">First name</a><div class="sep">|</div></li>
			<li><a href="<%=urls.url("browse","department")%>" class="nav">Department</a><div class="sep">|</div></li>
			<li><a href="<%=urls.url("browse","location")%>" class="nav">Location</a></li>
		</ul>
		<form id="nav_search_entire" action="<%=urls.url("search")%>" method="GET">
		<label for="nav_search"> Search:</label>
		<input type="text" name="query" size="31" maxlength="255" value="" id="nav_search" />
		<input type="submit" value = "Search" id="nav_search_button"></input>
		</form>
	</div>
</div>
	<% if (user == null) { %>
	
		<div id="user_bar">
			<a href="<%=urls.url("user","login")%>"> LOGIN </a>
		</div>
		<div id="home">
			<a href="<%=urls.url("index")%>"> HOME </a>
		</div>
	
	<% } else { %>
	
		<div id="welcome">
			<p> Welcome, <%=user.getFirstName()%></p>
		</div>
		<div id="user_bar">
			<a href="<%=urls.url("user","logout")%>"> LOGOUT </a>
		</div>
		<div id="home">
			<a href="<%=urls.url("index")%>"> HOME </a>
		</div>
		<div id="user_options">
			<ul id="user_options_list">
				<li> <a href="<%=urls.url("person",user.getUid(),"profile")%>"> My Profile </a> </li> 
				<li> <a href="<%=urls.url("user","bookmarks")%>"> My Bookmarks </a> </li> 
				<li> <a href="<%=urls.url("user","edit")%>"> Edit Profile </a> </li> 
			</ul>
		</div>
	<% } %>
