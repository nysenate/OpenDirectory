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
<<<<<<< HEAD
				<div class="splash_new"><h2 class="homeFAQ">Questions about Open Directory? See our <a href="faq/">FAQ</a></h2>
				  <br />
=======
				<div class="splash_new"><h2 class="homeText">Questions about Open Directory? See our <a href="<%=urls.url("faq")%>">FAQ</a></h2>
				 <!-- <br />
>>>>>>> 9594230af2746ed0e75b8effed56c3b1d851041e
				  <h2 class="homeUpdate">Recently Updated Profiles</h2>
				  <center>
				   <ul class="splashProfiles">
				    <li id="sp_1"><a href="" title=""><img src="" width="82.5" height="106.5" /></a></li>
				    <li id="sp_2"><a href="" title=""><img src="" width="82.5" height="106.5" /></a></li>
				    <li id="sp_3"><a href=" title=""><img src="" width="82.5" height="106.5" /></a></li>
				    <li id="sp_4"><a href="" title=""><img src="" width="82.5" height="106.5" /></a></li>
				  </ul>
				  </center> -->
				  </div>
			</div>

<jsp:include page="footer.jsp" />