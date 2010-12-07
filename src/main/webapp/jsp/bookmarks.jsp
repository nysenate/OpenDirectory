<%@ page language="java" import="java.util.ArrayList,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person"  %><%  
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
				<h> My Bookmarks</h>
				<br></br>
					<% if (user_bookmarks != null) { %>
						<% for(Person bookmark : user_bookmarks) {%>
						<div id="name_pic">
							<% if(bookmark.getPicture()!=null && !bookmark.getPicture().isEmpty()) { %>
								<img src="<%=bookmark.getPicture()%>" width="100" height="100">
							<% } else { %>
								<img src="<%=urls.url("img","einstein.jpg")%>" width="100" height="100">
							<% } %>
						</div>
						<div id="remove">		
								<a href="<%=urls.url("user","bookmarks","remove", bookmark.getUid())%>"> Remove From Bookmarks </a>
						</div>
						<div id="bookmark_info">
							<a href="<%=urls.url("person",bookmark.getUid(),"profile")%>"> <%=bookmark.getFullName()%></a>
							<p> <%=bookmark.getTitle() %> , <%=bookmark.getPhone() %></p>
							<a href="mailto:<%=bookmark.getEmail() %>"><%=bookmark.getEmail() %></a>
						</div>
						<% } %>
					<% } %>	
				</div>		
			</div>
		</body>
</html>