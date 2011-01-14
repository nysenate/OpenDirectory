<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	String error_message = (String)request.getAttribute("errorMessage");
	String error = (String)request.getAttribute("error");
	String phone = (String)request.getAttribute("phone2");
	String email = (String)request.getAttribute("email");
	String firstName = (String)request.getAttribute("firstName");
	String lastName = (String)request.getAttribute("lastName");


%>
<jsp:include page="header.jsp" />
			<div id="main" style="margin-top:50px;">
				<div id="edit_error" style="font-size:85%; width:60%; display:<%=error!= null ? "inherit" : "none" %>;"><%= error!=null ? "It looks like there may be a problem with one of your contactfields.  <br/>Please check to make sure you:<br/>" + error:"" %></div>
			
				<h2 style="margin-left:30px;">Register to access OpenDirectory</h2>
				<div id="main_center">
					<i>Note that this is currently only available to users with an email address ending in state.ny.us or ny.gov</i>
					<br/>
					<i>All fields required</i>
					<br/><br/>
					<% if(error_message != null) { %>
						<div id="error">
							<%=error_message %>
							<br></br>
						</div>
					<% } %>
					<div id="edit_container" style="margin:auto;">
						<div id="edit_form_field" > <!-- style="float:left" -->
							<form action="" method="POST">
								<ol>
									<li>
										<label id="external_input">Email</label>
										<input style="margin-left:34px;" value="<%=email != null ? email : "" %>" type="text" name="email1" size="31" maxlength="255" value=""/>
									</li>
									<li>
										<label id="external_input">Verify Email</label>
										<input style="margin-left:-11px;" value="" type="text" name="email2" size="31" maxlength="255" value=""/>
									</li>
									<li>
										<label id="external_input">First Name</label>
										<input style="margin-left:-3px;" value="<%=firstName != null ? firstName : "" %>" type="text" name="firstName" size="31" maxlength="255" value=""/>
									</li>
									<li>
										<label id="external_input">Last Name</label>
										<input style="margin-left:-2px;" value="<%=lastName != null ? lastName : "" %>" type="text" name="lastName" size="31" maxlength="255" value=""/>
									</li>
									<li>
										<label id="external_input">Phone</label>
										<input value="<%= phone != null ? phone : "(###) ###-####" %>" style="margin-left:30px;" type="text" name="phone2" size="31" maxlength="255" value=""/>
									</li>
									<li>
										<label id="external_input">Password</label>
										<input style="margin-left:6px;" type="password" name="pword1" size="31" maxlength="255" value=""/>	
									</li>
									<li>
										<label id="external_input">Verify Password</label>
										<input style="margin-left:-39px;" type="password" name="pword2" size="31" maxlength="255" value=""/>	
									</li>					
								</ol>
								<input type="submit" value="Register" id="submit_button"></input>
							</form>
						</div>
					</div>
				</div>
			</div>
<jsp:include page="footer.jsp" />
