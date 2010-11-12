<%@ page language="java" import="gov.nysenate.opendirectory.models.Person" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	Person user = (Person)request.getAttribute("user");
%>
<div id="header">
	<a href="/opendirectory/"> <div id="logo"></div> </a>
	<div id="nav_bar">Browse by:
		<ul>
			<li><a href="/opendirectory/browse/lastname" class="nav">Last name</a></li> |
			<li><a href="/opendirectory/browse/firstname" class="nav">First name</a></li> |
			<li><a href="/opendirectory/browse/department" class="nav">Department</a></li> |
			<li><a href="/opendirectory/browse/location" class="nav">Location</a></li> |
		</ul>
		<form id="nav_search_entire" action="/opendirectory/search" method="GET">
		<label for="nav_search"> Search:</label>
		<input type="text" name="query" size="31" maxlength="255" value="" id="nav_search" />
		<input type="submit" value = "Search" id="nav_search_button"></input>
		</form>
	</div>
</div>
	<% if (user == null) { %>
	
		<div id="user_bar">
			<a href="/opendirectory/user/login"> LOGIN </a>
		</div>
		<div id="home">
			<a href="/opendirectory/"> HOME </a>
		</div>
	
	<% } else { %>
	
		<div id="welcome">
			<p> Welcome, <%=user.getFirstName()%></p>
		</div>
		<div id="user_bar">
			<a href="/opendirectory/user/logout"> LOGOUT </a>
		</div>
		<div id="home">
			<a href="/opendirectory/"> HOME </a>
		</div>
		<div id="user_options">
			<ul id="user_options_list">
				<li> <a href="/opendirectory/person/<%= user.getUid() %>"> My Profile </a> </li> 
				<li> <a href=""> My Bookmarks </a> </li> 
				<li> <a href=""> Edit Profile </a> </li> 
			</ul>
		</div>
	<% } %>
