<%@ page language="java" import="gov.nysenate.opendirectory.models.Person" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	Person person = (Person)request.getAttribute("person");
%><!DOCTYPE html5>

<html>
<head>
<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
</head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=person.getFullName() %></title>
</head>
<body>
<div id="page">
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
					<input type="text" name="nav_search" size="31" maxlength="255" value="" id="nav_search"</input>
					<input type="button" value = "Search" id="nav_search_button"></input>
					</div>
				</div>
			</div>
			
<div id="main">
<b><%=person.getFullName()%></b>
<p>Title: <%=person.getTitle() %></p>
<p>Location: <%=person.getLocation() %></p>
<p>Department: <%=person.getDepartment() %></p>
<p>Phone: <%=person.getPhone() %></p>
<p>E-mail: <%=person.getEmail() %></p>

</div>

<div id="forward_back">
<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> | <a HREF="javascript:history.go(1)" class="forward_back">Forward</a>

</body>
</html>