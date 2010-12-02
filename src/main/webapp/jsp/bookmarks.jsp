<%@ page language="java" import="java.util.ArrayList,gov.nysenate.opendirectory.servlets.utils.UrlMapper,gov.nysenate.opendirectory.models.Person"  %><%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
    Person user = (Person)request.getAttribute("user");
    ArrayList<Person> user_bookmarks = user.getBookmarks();
%>
<html>
	<head>
		<title>Bookmark Page</title>
			<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
				<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	</head>
		<body>
			<div id="page">
				<jsp:include page="header.jsp" />
				<div id="main">
				<h> My Bookmarks </h>
					<% if (user_bookmarks != null) { %>
						<% for(Person bookmark : user_bookmarks) {%>
						<div id="name_pic">
								<img src="<%=urls.url("img","einstein.jpg")%>" width="100" height="100"/>
						</div>
						<div id="bookmark_info">
							<a href="<%=urls.url("person",bookmark.getUid())%>"> <%=bookmark.getFullName()%></a>
						</div>
						<% } %>
					<% } %>	
				</div>		
		</body>
</html>