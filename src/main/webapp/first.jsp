<%@ page language="java" import="java.util.HashMap,java.util.Collections,java.util.TreeSet,java.util.ArrayList,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	HashMap<String,ArrayList<Person>> people = (HashMap<String,ArrayList<Person>>)request.getAttribute("people");
%><html>
<head>
<link rel="stylesheet" type="text/css" href="style.css" />
<h1>SenBook</h1>
<h2>The NYSS Employee Information Tool</h2>
</head>

<body>

<div id="f_b">
<a HREF="javascript:history.go(-1)">Go back</a> | <a HREF="javascript:history.go(1)">Go forward</a>
</div>

<% for(String bucket : new TreeSet<String>(people.keySet()) ) { %>
	<h3><%=bucket%></h3>
	<ul>
		<% for(Person p : people.get(bucket)) { %>
			<li><%=p.getFullName()%></li>
		<% } %>
	</ul>
<% } %>



</body>
</html>