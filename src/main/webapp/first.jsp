<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%><html>
	<head>
		<title>First Name Browse</title>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>		
		<script type="text/javascript">
			$(document).ready( function() {
				$(".entity_button").each( function() {
					var button = $(this);
					var first = button.attr('id').split('_')[1];
					var list = $("#list_"+first);
					
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
				<% for(String first : new TreeSet<String>(people.keySet()) ) {
					StringTokenizer st = new StringTokenizer(first," .-_'&");
					String nospace = "";
					while( st.hasMoreElements()) nospace+=st.nextElement();
					st = new StringTokenizer(nospace,"/");
					nospace = "";
					while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
					<input type="button" value="+" id="button_<%=nospace%>" class="entity_button" /> <span class="entity_title"><%=first%></span> 
					<div id="<%=first%>"> 
						<ul id="list_<%=nospace%>" class="people">
							<% for(Person p : people.get(first)) { %>
										<li> <a href="/opendirectory/person/<%=p.getUid()%>" class="people_url"><%=p.getFullName()%></a></li>
							<% } %>
						</ul>
					</div>
				<% } %>
					<div id="user_bar">
					<a href="/opendirectory/user/login"> LOGIN </a>
					</div>
					<div id="home">
					<a href="/opendirectory/"> HOME </a>
					</div>
			</div>
	</body>
</html>