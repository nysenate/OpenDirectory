<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	ArrayList<Person> people = (ArrayList<Person>)request.getAttribute("people");
%>
<html>
	<head>
		<title>Complete User List</title>
	</head>
	<body>
		<table id="person_table">
		<% for( Person person : people ) { %>
			<tr>
				<td class="uid"><%=person.getUid()%></td>
				<td class="first_name"><%=person.getFirstName()%></td>
				<td class="last_name"><%=person.getLastName()%></td>
				<td class="full_name"><%=person.getFullName()%></td>
				<td class="department"><%=person.getDepartment()%></td>
				<td class="title"><%=person.getTitle()%></td>
			</tr>
		<% } %>
		</table>
	</body>
</html>