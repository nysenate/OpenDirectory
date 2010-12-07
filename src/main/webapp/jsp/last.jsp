<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person,gov.nysenate.opendirectory.utils.UrlMapper"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	UrlMapper urls = (UrlMapper)request.getAttribute("urls");
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%><html>
	<head>
		<title>Last Name Browse</title>
		<link rel="stylesheet" type="text/css" href="<%=urls.url("css","style.css")%>" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript">
			$(document).ready( function() {
				$(".entity_button").each( function() {
					var button = $(this);
					var last = button.attr('id').split('_')[1];
					var list = $("#list_"+last);
					
					$(this).toggle(
						function() {
							list.hide();
							return false;
						},
						function() {
							list.show();
							return false;
						}
					).click();
					
				});
			});
		</script>
	</head>
	<body>
		<div id="page">
		<jsp:include page="header.jsp" />			
		<div id="main">
			<% for(String last : new TreeSet<String>(people.keySet()) ) {
				StringTokenizer st = new StringTokenizer(last," .-_'&");
				String nospace = "";
				while( st.hasMoreElements()) nospace+=st.nextElement();
				st = new StringTokenizer(nospace,"/");
				nospace = "";
				while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
				<input type="button" value="+" id="button_<%=nospace%>" class="entity_button"></input> <span class="entity_title"><%=last%></span> 
				<div id="<%=last%>"> 
					<ul id="list_<%=nospace%>" class="people">
						<% for(Person p : people.get(last)) { %>
							<li> <a href="<%=urls.url("person",p.getUid(),"profile")%>" class="people_url"><%=p.getFullName()%></a></li>
						<% } %>
					</ul>
				</div>
			<% } %>
		</div>
	</body>
</html>