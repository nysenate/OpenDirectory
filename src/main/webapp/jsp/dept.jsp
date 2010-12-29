<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,HashMap<String,TreeSet<Person>>> orgs = (HashMap<String,HashMap<String,TreeSet<Person>>>)request.getAttribute("people");
	
	
	
%><jsp:include page="header.jsp" />
			<div id="main">
				<div id="main_regular">
			<% for(String org : new TreeSet<String>(orgs.keySet()) ) {
					HashMap<String,TreeSet<Person>> depts = orgs.get(org);
					
					if(depts.keySet().size() == 1 && depts.keySet().toArray()[0].equals(org)) {
						for(String department:new TreeSet<String>(depts.keySet())) {
							String nospace = department.replaceAll("[ ,\\._'&/]","");
							%>
							  <span id="button_<%=nospace%>" class="entity_button"></span><span class="entity_title"><%=department%></span> 
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
						  <span id="button_<%=orgRep%>" class="entity_button"></span><span class="entity_title"><%=org+" ( "+depts.size()+" units )"%></span> 
							<div id="<%=orgRep%>" class="entity_list" style="position:relative;top:10px;"> 
								<ul id="list_<%=orgRep %>" class="people">
									<% for(String department:new TreeSet<String>(depts.keySet())) { 
										String departmentRep = department.replaceAll("[ ,\\._'&/]","");
										
										%>										
										  <span id="button_<%=departmentRep%>" class="entity_button"></span><span class="entity_title"><%=department%></span> 
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
<jsp:include page="footer.jsp" />



