<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				<form id="all_search" action="<%=urls.url("search")%>" method="GET">
					<div id="search_entire">
						<label for="s"> Search for Employee:</label>
						<input type="text" name="query" size="31" maxlength="255" value="" id="s" /> <a href="#advancedsearch" id="advanced"> Advanced Search</a>
						<br></br>
						<input type="submit" value ="Search" id="search_button"></input>
						<br/>
						<br/>
					</div>
					<div id="advanced_search">
						
						<label for="s"> Search by First Name:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_first"></input>
						<br/>
						
						<label for="s"> Search by Last Name:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_last"></input>
						<br/>
						
						<label for="s"> Search by Department:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_department"></input>
						<br/>
						
						<label for="s"> Search by Location:</label>
						<input type="text" name="search" size="31" maxlength="255" value="" id="search_location"></input>
						<br></br>
						<input type="button" value ="Search" id="but"></input>
					</div>
				</form>
			</div>
			<div id="footer">
			</div>
<jsp:include page="footer.jsp" />