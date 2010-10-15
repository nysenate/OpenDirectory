<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%><html>
<head>
<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
</head>

<body>
<h1>SenBook</h1>
<h2>The NYSS Employee Information Tool</h2>
<%--
	char[] alpha = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
<% for (char letter : alpha) { %>
		<a href="#<%=letter%>"> <%=letter%> </a> |
<% } %>

<% for (char letter : alpha) {%>
		<a name="<%=letter%>"></a> <div id="<%=letter%>_Name"> <b> <%=letter%> </b> </div>
<% } %>

--%>

<% for(String last : new TreeSet<String>(people.keySet()) ) { %>

	<h3><%=last%></h3>
	<ul>
		<% for(Person p : people.get(last)) { %>
			<li> <a href="/opendirectory/person/<%=p.getUid()%>"><%=p.getFullName()%></a></li>
		<% } %>
	</ul>
<% } %>



<div id="f_b">
<a HREF="javascript:history.go(-1)">Go back</a> | <a HREF="javascript:history.go(1)">Go forward</a>
</div>

</body>
</html>
