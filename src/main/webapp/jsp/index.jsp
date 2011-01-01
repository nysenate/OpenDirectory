<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper,java.util.List,gov.nysenate.opendirectory.models.Person"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	List<Person> people = (List<Person>)request.getAttribute("frontPagePeople");

%><jsp:include page="header.jsp" />		
			<div id="main">
			  <div id="splash_logo"><img src="<%=urls.url("img","Open_Directory.png") %>"></img></div>
				<div id="splash_desc"> 
					<h2 class="homeText">Browse and search for people who have the skills you need and the interests you share in the New York State Senate.</h2>
					<form action="<%=urls.url("search")%>" method="GET">
						<input type="text" name="query" style="width:300px" autocomplete="off" value="" id="s" />
						<input type="submit" value ="Search" id="search_button"></input>
					</form>
					<div class="quickresult" id="quickresult-body" style="z-index:1;"></div>
				</div>
				<div class="splash_new">
					<h2 class="homeFAQ">Questions about Open Directory? See our <a href="faq/">FAQ</a></h2> 
				</div>
				<div class="splash_new" style="width:600px;">
					<% if(people != null && people.size() != 0) { %>
						 <h2 class="homeUpdate">Recently Updated Profiles</h2>
						 <center>
						 	 <ul class="splashProfiles">
							 <% for(Person person:people) { %>
								    <li><a href="<%=urls.url("person",person.getUid(),"profile") %>" title="<%=person.getTitle()%>"><img src="<%=person.getPicture() %>" width="82.5" height="106.5" /><%=person.getFullName() %></a></li>
							  <% } %>
						  	</ul>
						</center>
					  <% } %>
				</div>
			</div>

<jsp:include page="footer.jsp" />