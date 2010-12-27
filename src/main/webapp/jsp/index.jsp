<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				<div style="margin:0px auto;">
				<center>
					<div style="margin-right:418px;margin-top:10px;"><img src="<%=urls.url("img","Open-Directory.png") %>"></img></div>
					<div style="text-align:left;width:500px;"> 
						<h2 class="homeText">Browse and search for people who have the skills you need and<br/>
											the interests you share in the New York State Senate.</h2>
					</div>
					<div style="text-align:left;width:500px;margin-top:20px;margin-bottom:20px;"> 
						<form action="<%=urls.url("search")%>" method="GET">
							<input type="text" name="query" style="width:300px" autocomplete="off" value="" id="s" />
							<input type="submit" value ="Search" id="search_button"></input>
						</form>
						<div class="quickresult" id="quickresult-body"></div> 
					</div>		 
				</center>
				</div>
			</div>

<jsp:include page="footer.jsp" />