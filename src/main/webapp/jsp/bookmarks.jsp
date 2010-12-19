<%@ page language="java" import="java.util.TreeSet,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person"  %><%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
    Person user = (Person)request.getAttribute("user");
    TreeSet<Person> user_bookmarks = user.getBookmarks();
%><jsp:include page="header.jsp" />
				<div id="main">
				<h> My Bookmarks</h>
				<br></br>
					<% if (user_bookmarks != null) { %>
						<% for(Person bookmark : user_bookmarks) {%>
						<div class="bookmark">
							<div class="bookmark_pic">
								<% if(bookmark.getPicture()!=null && !bookmark.getPicture().isEmpty()) { %>
									<img src="<%=bookmark.getPicture()%>" width="82" height="106">
								<% } else { %>
									<img src="<%=urls.url("img","defaults","Gravatar-20.png")%>" width="82" height="106">
								<% } %>
							</div>
							<div class="bookmark_info">
								<a href="<%=urls.url("person",bookmark.getUid(),"profile")%>"> <%=bookmark.getFullName()%></a><br/>
								<%=bookmark.getTitle() %> , <%=bookmark.getPhone() %><br/>
								<a href="mailto:<%=bookmark.getEmail() %>"><%=bookmark.getEmail() %></a><br/><br/>
								<a href="<%=urls.url("user","bookmarks","remove", bookmark.getUid())%>"> Remove From Bookmarks </a>
							</div>
							<div class="clear"></div>
						</div>
						<% } %>
					<% } %>	
				</div>		
<jsp:include page="footer.jsp" />