<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%><jsp:include page="header.jsp" />
			<div id="main">
				<% for(String first : new TreeSet<String>(people.keySet()) ) {
					StringTokenizer st = new StringTokenizer(first," .-_'&");
					String nospace = "";
					while( st.hasMoreElements()) nospace+=st.nextElement();
					st = new StringTokenizer(nospace,"/");
					nospace = "";
					while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
					<input type="button" value="+" id="button_<%=nospace%>" class="entity_button" /> <span class="entity_title"><%=first%></span> 
					<div id="<%=first%>"> 
						<ul id="list_<%=nospace%>" class="people">
							<% for(Person p : people.get(first)) { %>
										<li> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
							<% } %>
						</ul>
					</div>
				<% } %>
			</div>
<jsp:include page="footer.jsp" />