<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	String error_message = (String)request.getAttribute("errorMessage");
	Person user = (Person)request.getAttribute("user");
	String title = (String)request.getAttribute("title");
	
%>
<jsp:include page="header.jsp" />
			<div id="main" style="margin-top:50px;">
				<h2 style="margin-left:30px;">Access to OpenDirectory for non Senate Government Employees</h2>
				<div id="main_center">					
					<% if(error_message != null) { %>
						<div id="error">
							<%=error_message %>
							<br></br>
						</div>
					<% } %>
					
					<div id="edit_form_field">
						<form action="" method="POST">
							<ol>
								<li>
									<label for="login_name">Username:</label>
									<input type="text" name="name" size="31" maxlength="255" value="" id="login_name"/>
								</li>
								<li>
									<label for="login_pword">Password:</label>
								<input type="password" name="password" size="31" maxlength="255" value="" id="login_pword"/>	
								</li>					
							</ol>
							<input type="submit" value="Login" id="login_button"></input>
						</form>
					</div>
				</div>
			</div>
<jsp:include page="footer.jsp" />
			