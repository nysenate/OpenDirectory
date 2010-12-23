<%@ page language="java" import="gov.nysenate.opendirectory.utils.UrlMapper"  %><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");

%><jsp:include page="header.jsp" />		
			<div id="main">
				<div id="main_center">
					<form action="<%=urls.url("search")%>" method="GET">
							<div id="edit_form_field">
								<ol>
									<li>
										<label for="s"> Search for Employee:</label>
										<input type="text" name="query" size="31" maxlength="255" value="" id="s" />
									</li>
								</ol>
								<input type="submit" value ="Search" id="search_button"></input>
							</div>
					</form>
				</div>
			</div>

<jsp:include page="footer.jsp" />