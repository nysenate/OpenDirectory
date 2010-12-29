<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%! @SuppressWarnings("unchecked") %><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
	String search_query = (String)request.getAttribute("query");
	boolean bestMatch = false;
%><jsp:include page="header.jsp" />
			<div id="main">
				<div id="main_regular">
				
					<h2> Search Results for:</h2> <h2 id="query"> <%= search_query %> </h2>
						<% if (results != null) { 
							if(results.size() == 0) {
								%><p> Your Search did not return any results</p><%
							}
							else {
								Person scorePerson = results.iterator().next();
								
								if(scorePerson.getScore() > 1) {
									bestMatch = true;
								}
							}
							
							%> <div id="search_best"> <%
							for( Person result : results ) { 
								
								
								if(result.getScore() < 1 && bestMatch) {
									bestMatch = false;
									%>
										</div>
										<div id="show_search_secondary"><h2>Click here to show partial matches</h2></div>
										<div id="search_secondary">
									<%
								}							
								%>
								<div class="result">
									<div class="result_pic">
										<% if(result.getPicture()!=null && !result.getPicture().isEmpty()) { %>
											<img src="<%=result.getPicture()%>" width="55" height="71">
										<% } else { %>
											<img src="<%=urls.url("img","defaults","Gravatar-10.png")%>" width="55" height="71">
										<% } %>
									</div>
									<div class="result_info">
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
									<div class="clear"></div>
								</div>
							<% } %>
						<% } else { %>
							<%if (results == null) { %>
							
							<p> Your Search did not return any results</p>
							
							<%} %>
						<% } %>
				</div>
			</div>
<jsp:include page="footer.jsp" />