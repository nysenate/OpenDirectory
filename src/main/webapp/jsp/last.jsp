<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.utils.UrlMapper,gov.nysenate.opendirectory.models.Person, gov.nysenate.opendirectory.utils.CachedContentManager, gov.nysenate.opendirectory.utils.Request"  %>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache" %>
<%
	Request self = (Request)request.getAttribute("self");
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
%>

<jsp:include page="header.jsp" />

	<cache:cache key="<%=CachedContentManager.getCacheKey(CachedContentManager.BrowseType.LAST_NAME, self) %>"  time="3600" scope="application">		
		
		<%  HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people"); %>
		
		<div id="main">
			<div id="main_regular">
			<% for(String last : new TreeSet<String>(people.keySet()) ) {
				StringTokenizer st = new StringTokenizer(last," ,.-_'&");
				String nospace = "";
				while( st.hasMoreElements()) nospace+=st.nextElement();
				st = new StringTokenizer(nospace,"/");
				nospace = "";
				int count=1;
				while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
				 <div class="button_div"><span id="button_<%=nospace%>" class="entity_button"></span><span class="entity_title"><%=last%></span> </div>
				<div id="<%=last%>" class="entity_list"> 
					<ul id="list_<%=nospace%>" class="people">
						<% for(Person p : people.get(last)) { %>
							<li class="<%=((count++%2==0) ? "even" : "odd" )%>"> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getLastName()+", "+p.getFirstName()%></a></li>
						<% } %>
					</ul>
				</div>
				<br/>
			<% } %>
			</div>
		</div>
	</cache:cache>
<jsp:include page="footer.jsp" />