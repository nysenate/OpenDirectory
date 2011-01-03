<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
	
%><jsp:include page="header.jsp" />
			<div id="main">
				<div id="main_regular">
					<% for(String loc : new TreeSet<String>(people.keySet()) ) {
						StringTokenizer st = new StringTokenizer(loc," ,.-_'&");
						String nospace = "";
						while( st.hasMoreElements()) nospace+=st.nextElement();
						st = new StringTokenizer(nospace,"/");
						nospace = "";
						int count=1;
						while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
						
						 <div class="button_div"><span id="button_<%=nospace%>" class="entity_button"></span><span class="entity_title"><%=loc%></span> </div>
						<div id="<%=loc%>" class="entity_list"> 
							<ul id="list_<%=nospace%>" class="people">
								<% for(Person p : people.get(loc)) { %>
									<li class="<%=((count++%2==0) ? "even" : "odd" )%>"> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
								<% } %>
							</ul>
						</div>
						<br/>
					<% } %>
					<% if(people.isEmpty()) { %>
						<div id="main_center">You must be logged in to see this content.</div>
					<% } %>
				</div>
			</div>
<jsp:include page="footer.jsp" />