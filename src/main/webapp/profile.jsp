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
<h1>SenBook</h1>
<h2>The NYSS Employee Information Tool</h2>
<div id="f_b">
<a HREF="javascript:history.go(-1)">Go back</a> | <a HREF="javascript:history.go(1)">Go forward</a>
</div>
<b><%=person.getFullName()%></b>
<p>Title: <%=person.getTitle() %></p>
<p>Location: <%=person.getLocation() %></p>
<p>Department: <%=person.getDepartment() %></p>
<p>Phone: <%=person.getPhone() %></p>
<p>E-mail: <%=person.getEmail() %></p>

</body>
</html>