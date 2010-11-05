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
		<div id="nav_search_entire">
		<label for="nav_search"> Search:</label>
		<input type="text" name="nav_search" size="31" maxlength="255" value="" id="nav_search" />
		<input type="button" value = "Search" id="nav_search_button"></input>
		</div>
	</div>
		<div id="forward_back">
		<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> | <a HREF="javascript:history.go(1)" class="forward_back">Forward</a>
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
	<% } %>
