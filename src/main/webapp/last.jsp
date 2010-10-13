<%
	char[] alpha = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/OpenDirectory/style.css" />
<h1>SenBook</h1>
<h2>The NYSS Employee Information Tool</h2>
</head>

<body>


<% for (char letter : alpha) {%>
		<a href="#<%=letter%>"> <%=letter%> </a> |
<% } %>

<% for (char letter : alpha) {%>
		<a name="<%=letter%>"></a> <div id="<%=letter%>_Name"> <b> <%=letter%> </b> </div>
<% } %>
 



<div id="f_b">
<a HREF="javascript:history.go(-1)">Go back</a> | <a HREF="javascript:history.go(1)">Go forward</a>
</div>

</body>
</html>
