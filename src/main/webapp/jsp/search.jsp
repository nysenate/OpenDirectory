<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%! @SuppressWarnings("unchecked") %><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
	String search_query = (String)request.getAttribute("query");
%><jsp:include page="header.jsp" />
			<div id="main">
				<h2> Search Results for:</h2> <h2 id="query"> <%= search_query %> </h2>
					<% if (results != null) { %>
						<% for( Person result : results ) { %>
						<div id="name_pic">
							<% if(result.getPicture()!=null && !result.getPicture().isEmpty()) { %>
								<img src="<%=result.getPicture()%>" width="100" height="100">
							<% } else { %>
								<img src="<%=urls.url("img","einstein.jpg")%>" width="100" height="100">
							<% } %>
						</div>
						<div id="search_info">
							<a href="<%=urls.url("person",result.getUid(),"profile")%>"> <%=result.getFullName()%></a>
							<%
								String lineone = "";
								if(result.getTitle()!=null && !result.getTitle().isEmpty())
									lineone+=result.getTitle();
								if(result.getLocation()!=null && !result.getLocation().isEmpty()) {
									if(!lineone.isEmpty())
										lineone+=", ";
									lineone+=result.getLocation();
								}
								if(result.getDepartment()!=null && !result.getDepartment().isEmpty()) {
									if(!lineone.isEmpty())
										lineone+=" - ";
									lineone+=result.getDepartment();
								}
								lineone =  "<p>"+lineone+"</p>";
								out.println(lineone);
							%>
						</div>
						<% } %>
					<% } else { %>
						<%if (results == null) { %>
						
						<p> Your Search did not return any results</p>
						
						<%} %>
					<% } %>
			</div>
<jsp:include page="footer.jsp" />