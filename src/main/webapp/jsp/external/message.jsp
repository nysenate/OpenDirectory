<%@ page language="java" import="gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	String error = (String)request.getAttribute("error");
	String message = (String)request.getAttribute("message");
	String header = (String)request.getAttribute("header");
	
	if(header ==  null && message == null && error == null) {
		response.sendRedirect(urls.url("index"));
	}

%>
<jsp:include page="header.jsp" />
			<div id="main" style="margin-top:50px;">			
				<h2 style="margin-left:30px;"><%=header %></h2>
				<div id="main_center">
					<br/><br/>
					<% if(error != null) { %>
						<div id="error">
							<%=error %>
							<br></br>
						</div>
					<% } %>
					<% if(message != null) { %>
							<div style="margin-left:30px;">
								<%=message %>
							</div>
							<br></br>
					<% } %>
					
					</div>
				</div>
<jsp:include page="footer.jsp" />

