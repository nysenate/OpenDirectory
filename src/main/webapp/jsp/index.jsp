<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

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
			</div>

<jsp:include page="footer.jsp" />