<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
	
%><jsp:include page="header.jsp" />
			<div id="main">
			<% for(String department : new TreeSet<String>(people.keySet()) ) {
					StringTokenizer st = new StringTokenizer(department," ,.-_'&");
					String nospace = "";
					while( st.hasMoreElements()) nospace+=st.nextElement();
					st = new StringTokenizer(nospace,"/");
					nospace = "";
					while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
					<span id="button_<%=nospace%>" class="entity_button"></span><span class="entity_title"><%=department%></span> 
					<div id="<%=department%>" class="entity_list"> 
						<ul id="list_<%=nospace%>" class="people">
							<% for(Person p : people.get(department)) { %>
								<li> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
							<% } %>
						</ul>
					</div>
					<br/>
			<% } %>
			</div>
<jsp:include page="footer.jsp" />



