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
      <script type="text/javascript" src="<%=urls.url("jsp", "external", "js","jquery.validate.min.js")%>"></script>
      <script type="text/javascript" src="<%=urls.url("jsp", "external", "js","additional-methods.js")%>"></script>
      <script type="text/javascript" src="<%=urls.url("jsp", "external", "js","register-validate.js")%>"></script>
      <script type="text/javascript" src="<%=urls.url("jsp", "external", "js","jquery.validate.password.js")%>"></script>
      <link rel="stylesheet" type="text/css" href="<%=urls.url("jsp", "external", "css","jquery.validate.password.css")%>">
      <link rel="stylesheet" type="text/css" href="<%=urls.url("jsp", "external", "css","register.css")%>">
      
      
      
      
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
						  
							<form id="external_registration" name="external_registration" method="post" action="#">

                <!--Email Address-->
                <label class="external_input"for="email">Email Address</label>
                <input id="email" name="email" type="text" value="<%=email != null ? email : "" %>"  size="31" maxlength="255" tabindex="1" style="margin-left:34px;"/>
                <br />

                <!--Verify Email Address-->
                <label class="external_input"for="email_verify">Verify Email Address</label>
                <input id="email_verify" name="email_verify" type="text" value="<%=email != null ? email : "" %>" value="" size="31" maxlength="255" tabindex="2" style="margin-left:-11px;"/>
                <br />

                <!--First Name-->
                <label class="external_input"for="firstname">First Name</label>
                <input id="firstname" name="firstname" type="text" value="<%=firstName != null ? firstName : "" %>" size="31" maxlength="255" tabindex="3" style="margin-left:-3px;"/ >
                <br />

                <!--Last Name-->
                <label class="external_input"for="lastname">Last Name</label>
                <input id="lastname" name="lastname" type="text" value="<%=lastName != null ? lastName : "" %>" size="31" maxlength="255" tabindex="4" style="margin-left:-2px;"/>
                <br />

                <!--Phone Number-->
                <label class="external_input"for="phone">Phone Number</label>
                <input id="phone" name="phone" type="text" value="<%= phone != null ? phone : "(###) ###-####" %>" style="margin-left:30px;" size="31" maxlength="255" tabindex="5" style="margin-left:30px;"/>
                <br />

                <!--Password-->
                <label class="external_input"for="password">Password</label>
                <input id="password" name="password" type="password" value="" size="31" maxlength="255" tabindex="6" style="margin-left:6px;"/>
                <div class="password-meter">
                  <div class="password-meter-message"></div>
                  <div class="password-meter-bg">
                    <div class="password-meter-bar"></div>
                  </div>
                </div>
                <br />

                <!--Verify Password-->
                <label class="external_input"for="password_verify">Verify Password</label>
                <input id="password_verify" name="password" type="password" value="" size="31" maxlength="255" tabindex="7" style="margin-left:-39px;"/>
                <br />

                <!--Submit-->
                <input id="submit" name="submit" type="submit" value="Submit" size="31" maxlength="255" tabindex="8" />

              </form>
							
						</div>
					</div>
				</div>
			</div>
<jsp:include page="footer.jsp" />
