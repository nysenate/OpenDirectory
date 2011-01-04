<%@ page language="java" import="java.util.TreeSet,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person"  %><%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
    Person user = (Person)request.getAttribute("user");
    TreeSet<Person> user_bookmarks = user.getBookmarks();
%><jsp:include page="header.jsp" />
				<div id="main">
				<div id="main_regular">
				<h> My Bookmarks</h>
				<br></br>
					<% if (user_bookmarks != null && !user_bookmarks.isEmpty()) { %>
						<% for(Person bookmark : user_bookmarks) { %>
						<div class="bookmark">
							<div class="bookmark_pic">
								<% if(bookmark.getPicture()!=null && !bookmark.getPicture().isEmpty()) { %>
									<img src="/uploads/avatars/thumb/<%=bookmark.getPicture()%>" width="55" height="71">
								<% } else { %>
									<img src="<%=urls.url("img","default_gravatar.png")%>" width="55" height="71">
								<% } %>
							</div>
							<div class="bookmark_info">
								<a href="<%=urls.url("person",bookmark.getUid(),"profile")%>"> <%=bookmark.getFullName()%></a>
									<%
										String lineone = "";
										if(bookmark.getTitle()!=null && !bookmark.getTitle().isEmpty())
											lineone+=bookmark.getTitle();
										if(bookmark.getLocation()!=null && !bookmark.getLocation().isEmpty()) {
											if(!lineone.isEmpty())
												lineone+=", ";
											lineone+=bookmark.getLocation();
										}
										if(bookmark.getDepartment()!=null && !bookmark.getDepartment().isEmpty()) {
											if(!lineone.isEmpty())
												lineone+=" - ";
											lineone+=bookmark.getDepartment();
										}
										lineone =  "<p>"+lineone+"</p>";
										out.println(lineone);
									%>
								<a href="<%=urls.url("user","bookmarks","remove", bookmark.getUid())%>"> Remove From Bookmarks </a>
							</div>
							<div class="clear"></div>
							
							
						</div>
						<% } %>
					<% } else { %>	
						<i>It looks like you don't have any bookmarks.</i>
						<% } %>
					</div>
				</div>
<jsp:include page="footer.jsp" />