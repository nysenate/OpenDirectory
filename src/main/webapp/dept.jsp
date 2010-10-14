<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%><html>
<head>
<link rel="stylesheet" type="text/css" href="/OpenDirectory/style.css" />
</head>

<body>
<h1>SenBook</h1>
<h2>The NYSS Employee Information Tool</h2>
<div id="f_b">
<a HREF="javascript:history.go(-1)">Go back</a> | <a HREF="javascript:history.go(1)">Go forward</a>
</div>

<% for(String bucket : new TreeSet<String>(people.keySet()) ) { %>
	<h3><%=bucket%></h3>
	<ul>
		<% for(Person p : people.get(bucket)) { %>
			<li>(<%=p.getDepartment()%>) <%=p.getFullName()%></li>
		<% } %>
	</ul>
<% } %>

</body>
</html>