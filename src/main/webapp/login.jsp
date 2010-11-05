<html>
	<head>
		<title>Login Page</title>
		<link rel="stylesheet" type="text/css" href="/opendirectory/style.css" />
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
	</head>
	<body>
		<div id="page">
			<jsp:include page="header.jsp" />
			<div id="main">
				<form id="login_entire" action="/opendirectory/user/login" method="POST">
					<label for="login_name">Username:</label>
					<input type="text" name="name" size="31" maxlength="255" value="" id="login_name"/>
					<br></br>
					<label for="login_pword">Password:</label>
					<input type="password" name="password" size="31" maxlength="255" value="" id="login_pword"/>
					<br></br>
					<input type="submit" value="Login" id="login_button"></input>
				</form>
				<div id="user_bar">
				<a href="/opendirectory/user/login"> LOGIN </a>
				</div>
				<div id="home">
				<a href="/opendirectory/"> HOME </a>
				</div>
			</div>
			
		<div id="forward_back">
			<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> | <a HREF="javascript:history.go(1)" class="forward_back">Forward</a>
		</div>
</body>
</html>