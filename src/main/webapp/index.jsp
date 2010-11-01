<html>
	<head>
		<title>Index Page</title>
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
					<input type="text" name="nav_search" size="31" maxlength="255" value="" id="nav_search"</input>
					<input type="button" value = "Search" id="nav_search_button"></input>
					</div>
				</div>
			</div>
			
			<div id="main">
				<div id="search_entire">
				<form></form>
				<label for="s"> Search for Employee:</label>
				<input type="text" name="search" size="31" maxlength="255" value="" id="s"</input> <a href="#advancedsearch" id="advanced"> Advanced Search</a>
				<br></br>
				<input type="button" value ="Search" id="search_button"></input>
				</br>
				</br>
				</div>
					<div id="advanced_search">
					
					<label for="s"> Search by First Name:</label>
					<input type="text" name="search" size="31" maxlength="255" value="" id="search_first"></input>
					<br/>
					
					<label for="s"> Search by Last Name:</label>
					<input type="text" name="search" size="31" maxlength="255" value="" id="search_last"></input>
					<br/>
					
					<label for="s"> Search by Department:</label>
					<input type="text" name="search" size="31" maxlength="255" value="" id="search_department"></input>
					<br/>
					
					<label for="s"> Search by Location:</label>
					<input type="text" name="search" size="31" maxlength="255" value="" id="search_location"></input>
					<br></br>
					<input type="button" value ="Search" id="but"></input>
					</div>
				</form>
			</div>
			
			<div id="footer">
			</div>
		</div>
</div>
<div id="forward_back">
<a HREF="javascript:history.go(-1)" class="forward_back">Back</a> | <a HREF="javascript:history.go(1)" class="forward_back">Forward</a>
</div>
</body>
</html>

	<script type="text/javascript">
		$(document).ready( function() {
	
			$("#advanced").toggle(
				function() {
					$("#search_button").show();
					$("#advanced_search").hide();
					return false;
				},
				function() {
					$("#search_button").hide();
					$("#advanced_search").show();
					return false;
				}
			).click();
		});
	
	</script>
