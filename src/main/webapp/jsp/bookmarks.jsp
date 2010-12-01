<%@ page language="java" import="gov.nysenate.opendirectory.servlets.utils.UrlMapper"  %><%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
    Person user = (Person)request.getAttribute("user");
    HashMap <String, String> user_bookmark = user.getBookMarks();
    
    for(String uid : user_bookmark.keySet()) {
    	user_bookmark.get(uid);
    }
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
					<% if (user_bookmark != null) { %>
						<% for(String uid : user_bookmark.keySet()) {%>
						<div id="name_pic">
								<img src="<%=urls.url("img","einstein.jpg")%>" width="100" height="100"/>
						</div>
						<div id="bookmark_info">
							<a href="<%=urls.url("person",user_bookmark.getUid())%>"> <%=user_bookmark.getFullName()%></a>
						</div>		
				</div>		
		</body>
</html>