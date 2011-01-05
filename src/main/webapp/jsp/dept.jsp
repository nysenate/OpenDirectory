<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person, gov.nysenate.opendirectory.utils.CachedContentManager, gov.nysenate.opendirectory.utils.Request"  %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%
	Request self = (Request)request.getAttribute("self");
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
%>

<jsp:include page="header.jsp" />

	<cache:cache key="<%=CachedContentManager.getCacheKey(CachedContentManager.BrowseType.DEPARTMENT, self) %>"  time="3600" scope="application">
			
			<%  HashMap<String,HashMap<String,TreeSet<Person>>> orgs = (HashMap<String,HashMap<String,TreeSet<Person>>>)request.getAttribute("people"); %>
			
			<div id="main">
				<div id="main_regular">
			<% for(String org : new TreeSet<String>(orgs.keySet()) ) {
					HashMap<String,TreeSet<Person>> depts = orgs.get(org);
					
					if(depts.keySet().size() == 1 && depts.keySet().toArray()[0].equals(org)) {
						for(String department:new TreeSet<String>(depts.keySet())) {
							String nospace = department.replaceAll("[ ,\\._'&/]","");
							%>
							  <div class="button_div"><span id="button_<%=nospace%>" class="entity_button"></span><span class="entity_title"><%=department%></span></div>
								<div id="<%=department%>" class="entity_list"> 
									<ul id="list_<%=nospace%>" class="people">
										<% int count = 1;
										   for(Person p : depts.get(department)) { %>
											<li class="<%=((count++%2==0) ? "even" : "odd" )%>"> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
										<% } %>
									</ul>
								</div>
							<br/> <%
						}
					}
					else {
						String orgRep = org.replaceAll("[ ,\\._'&]","-");
						
						%>
						  <div class="button_div"> <span id="button_<%=orgRep%>" class="entity_button"></span><span class="entity_title"><%=org+" ( "+depts.size()+" units )"%></span> </div>
							<div id="<%=orgRep%>" class="entity_list" style="position:relative;top:10px;"> 
								<ul id="list_<%=orgRep %>" class="people">
									<% for(String department:new TreeSet<String>(depts.keySet())) { 
										String departmentRep = department.replaceAll("[ ,\\._'&/]","");
										
										%>										
										   <div class="button_div"><span id="button_<%=departmentRep%>" class="entity_button"></span><span class="entity_title"><%=department%></span> </div>
											<div id="<%=departmentRep%>" class="entity_list"> 											
												<ul id="list_<%=departmentRep %>" class="people">
													<% 
														int count = 1;
														for(Person p : depts.get(department)) { %>
															<li class="<%=((count++%2==0) ? "even" : "odd" )%>"> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
													<% } %>
												</ul>
											</div>
										<br/> <%
									 }	%>
								</ul>
							</div>
						<br/> <%
					}
			} %>
			</div>
		</div>
	</cache:cache>
		
<jsp:include page="footer.jsp" />



