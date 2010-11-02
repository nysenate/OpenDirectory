<html>
	<head>
		<title>Login Page</title>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
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
					<input type="text" name="nav_search" size="31" maxlength="255" value="" id="nav_search"/>
					<input type="button" value = "Search" id="nav_search_button"></input>
					</div>
				</div>
			</div>
			
			<div id="main">
				<form id="login_entire">
				<label for="login_name">Username:</label>
				<input type="text" name="name" size="31" maxlength="255" value="" id="login_name"/>
				<br></br>
				<label for="login_pword">Password:</label>
				<input type="password" name="password" size="31" maxlength="255" value="" id="login_pword"/>
				<br></br>
				<input type="button" value = "Login" id="login_button"></input>
				</form>
				<div id="user_bar">
				<a href="/opendirectory/login.jsp"> LOGIN </a>
				</div>
			</div>
			
		<div id="forward_back">
			<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> | <a HREF="javascript:history.go(1)" class="forward_back">Forward</a>
		</div>
</body>
</html>