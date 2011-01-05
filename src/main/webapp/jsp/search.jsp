<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,java.util.ArrayList,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%! @SuppressWarnings("unchecked") %><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	ArrayList<Person> results = (ArrayList<Person>)request.getAttribute("results");
	String search_query = (String)request.getAttribute("query");
	boolean bestMatch = false;
%><jsp:include page="header.jsp" />
			<div id="main">
			  <div class="ratio_right" id="search_left">
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
							
							%> <div id="search_best">
							  <ul class="search_list"> <%
							
							for( Person result : results ) { 
								
								
								if(result.getScore() < 1 && bestMatch) {
									bestMatch = false;
									%>
									  </ul> 
										</div>
										<div id="show_search_secondary"><h2>Click here to show partial matches</h2></div>
										<div id="search_secondary">
										<ul class="search_list">
									<%
								}							
								%>
							  <li class="result">
									<span class="result_pic">
										<% if(result.getPicture()!=null && !result.getPicture().isEmpty()) { %>
											<img src="/uploads/avatars/thumb/<%=result.getPicture()%>" width="55" height="71">
										<% } else { %>
											<img src="<%=urls.url("img", "default_gravatar.png")%>" width="55" height="71">
										<% } %>
									</span>
									<span class="result_info">
										<h3 class="search_name" id="uid_<%=result.getUid()%>"> <%=result.getFullName()%></h3>
										<%
											String lineone = "";
											if(result.getTitle()!=null && !result.getTitle().isEmpty())
												lineone+=result.getTitle();
											if(result.getDepartment()!=null && !result.getDepartment().isEmpty()) {
												if(!lineone.isEmpty())
													lineone+="<br/>";
												lineone+=result.getDepartment();
											}
											lineone =  "<p>"+lineone+"</p>";
											out.println(lineone);
										%>
  								</span>
								</li>
							<% } %>
						<% } else { %>
							<%if (results == null) { %>
							
							<p> Your Search did not return any results</p>
							
							<%} %>
						<% } %>
						</ul>
				</div>
			  </div>
			</div>
			<!--Start Right Side-->
			<div class="ratio_left" id="search_right">
		    <div class="search_preview">
		      <img src='/opendirectory/img/default_gravatar.png' title="Samuel Richard" height="106.5" width="82.5" id="search_preview_image">
		      <h3 id="search_preview_name">Samuel Provenzano-Heal</h3><br/>
		      <span id="search_preview_title">Web Programmer</span><br/>
		      <span id="search_preview_office">Office of the Chief Information Officer</span>
		      <br/>
		      <span id="senate_location">New York</span><br/>
		      <span id="senate_email">richard@nysenate.gov</span><br/>
		      <span id="additional_email">sam@snug.ug</span><br/>
		      <span id="senate_phone">212-298-7501</span><br/>
		      <span  id="additional_phone">775-476-8484</span><br/>
		      <br/>
		      <a href="/opendirectory/person/richard/profile">View Full Profile...</a>
		    </div>
		  </div>
<jsp:include page="footer.jsp" />