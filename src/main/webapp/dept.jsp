<%@ page language="java" import="java.util.HashMap,java.util.TreeSet,java.util.StringTokenizer,gov.nysenate.opendirectory.models.Person"  %>
<%! @SuppressWarnings("unchecked") %>
<%  
	HashMap<String,TreeSet<Person>> people = (HashMap<String,TreeSet<Person>>)request.getAttribute("people");
%>
<html>
	<head>
		<title>Department Browse</title>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript">
			$(document).ready( function() {
				$(".entity_button").each( function() {
					var button = $(this);
					var department = button.attr('id').split('_')[1];
					var list = $("#list_"+department);
					
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
			<div id="header">
				<a href="/opendirectory/"> <div id="logo"></div> </a>
				<div id="nav_bar">Browse by:
					<ul>
						<li><a href="/opendirectory/browse/lastname" class="nav">Last name</a></li> |
						<li><a href="/opendirectory/browse/firstname" class="nav">First name</a></li> |
						<li><a href="/opendirectory/browse/department" class="nav">Department</a></li> |
						<li><a href="/opendirectory/browse/location" class="nav">Location</a></li> |
					</ul>
					<div id="nav_search_entire">
					<label for="nav_search"> Search:</label>
					<input type="text" name="nav_search" size="31" maxlength="255" value="" id="nav_search" />
					<input type="button" value = "Search" id="nav_search_button" />
					</div>
				</div>
			</div>
			<div id="main">
			<% for(String department : new TreeSet<String>(people.keySet()) ) {
					StringTokenizer st = new StringTokenizer(department," .-_'&");
					String nospace = "";
					while( st.hasMoreElements()) nospace+=st.nextElement();
					st = new StringTokenizer(nospace,"/");
					nospace = "";
					while( st.hasMoreElements()) nospace+="-"+st.nextElement();	%>
					
					<input type="button" value="+" id="button_<%=nospace%>" class="entity_button"></input> <span class="entity_title"><%=department%></span> 
					<div id="<%=department%>"> 
						<ul id="list_<%=nospace%>" class="people">
							<% for(Person p : people.get(department)) { %>
								<li> <a href="/opendirectory/person/<%=p.getUid()%>" class="people_url"><%=p.getFullName()%></a></li>
							<% } %>
						</ul>
					</div>
			<% } %>
				<div id="user_bar">
				<a href="/opendirectory/login.jsp"> LOGIN </a>
				</div>
			</div>
		</div>
		<div id="forward_back">
			<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> |
			<a HREF="javascript:history.go(1)" class="forward_back">Forward</a>
		</div>
	</body>
</html>



