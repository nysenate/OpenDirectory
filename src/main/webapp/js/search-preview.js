$(document).ready(function() {
	$('.search_preview').css('visibility','hidden');
	
	$(".search_name").click(function() {
		getPerson(event.target.id.split("_")[1], writeToSearchBox)
	});
	
	writeToSearchBox = (function(person) {
		var html = "";
		html += "<img src=" + (person.picture != null && person.picture != '' ? "/uploads/avatars/profile/"
				+ person.picture : "\"/opendirectory/img/default_gravatar.png\"") + " title=\"" +
				person.name + "\" height=\"106.5\" width=\"82.5\" id=\"search_preview_image\">"
	    	+ "<h3 id=\"search_preview_name\">" + person.name + "</h3><br/>"
	    	+ "<span id=\"search_preview_title\">" + person.title + "</span><br/>"
	    	+ "<span id=\"search_preview_office\">" + person.department + "</span>"
	    	+ "<br/>"
	    	+ "<span id=\"senate_location\">" + person.location + "</span><br/>"
	    	+ "<span id=\"senate_email\">" + person.email + "</span><br/>"
	    	+ "<span id=\"additional_email\">" + person.email2 + "</span><br/>"
	    	+ "<span id=\"senate_phone\">" + person.phone + "</span><br/>"
	    	+ "<span  id=\"additional_phone\">" + person.phone2 + "</span><br/>"
	    	+ "<br/>"
	    	+ "<a href=\"/opendirectory/person/" + person.uid + "/profile\">View Full Profile...</a>";
		$('.search_preview').html(html);
		$('.search_preview').css('visibility','visible');
	});
	
	writePerson = (function(data, funct) {
		var pl = $(data).find('person');

		var person = new Array();
		
		person.picture = $(pl).find('picture').html();
		person.name = $(pl).find('fullName').html();
		person.title = $(pl).find('title').html();
		person.department = $(pl).find('department').html();
		person.location = $(pl).find('location').html();
		person.email = $(pl).find('email').html();
		person.email2 = $(pl).find('email2').html();
		person.phone = $(pl).find('phone').html();
		person.phone2 = $(pl).find('phone2').html();
		person.uid = $(pl).find('uid').html();
		
		funct(person);
	});
  
	getPerson = (function(uid, funct) {
		var query = "/opendirectory/api/1.0/person/uid/" + uid + ".xml";
		var person = new Array();
		$.get(query, function(data) {
			writePerson(data,funct);
		});
	});
});

