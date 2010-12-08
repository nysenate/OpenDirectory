<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%

	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	String error_message = (String)request.getAttribute("errorMessage");
	
%><jsp:include page="header.jsp" />
			<div id="main">
				<div id="error">
				<% if(error_message != null) { %>
					
					<%=error_message %>
					<br></br>
				<% } %>
				</div>
			
				<form id="login_entire" action="<%=urls.url("user","login")%>" method="POST">
					<label for="login_name">Username:</label>
					<input type="text" name="name" size="31" maxlength="255" value="" id="login_name"/>
					<br></br>
					<label for="login_pword">Password:</label>
					<input type="password" name="password" size="31" maxlength="255" value="" id="login_pword"/>
					<br></br>
					<input type="submit" value="Login" id="login_button"></input>
				</form>
			</div>
<jsp:include page="footer.jsp" />