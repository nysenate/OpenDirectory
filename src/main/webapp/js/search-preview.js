$(document).ready(function() {
	$('.search_preview').css('display','none');
	
	var browser = navigator.appName;
	var webkit = $.browser.webkit;
	var firefox = false;
	
	if (browser == 'Netscape' && webkit != true) {
	  var firefox = true;
	} else {
	  var firefox = false;
	}
	
	var currentParent = '';
	var previousParent = '';
	
	$(".search_name").click(function() {
	  var position = $(this).position().top-25;
	  if (firefox) {
	    position -= 40;
	  }
	  currentParent = $(this).closest('span').parent();
	  currentParent.attr('old-background', currentParent.css('background'));
	  currentParent.css('background', '#E2DED5');
		$('html,body').animate({scrollTop:position},500);
		$('.search_preview').css('margin-top', position);
		getPerson($(this).attr('id').split("_")[1], writeToSearchBox)
	});
	
	writeToSearchBox = (function(person) {
	  if (currentParent != previousParent || previousParent == '') {
	    if (previousParent != '') {
	      previousParent.css('background',  currentParent.attr('old-background') /*'#fff'*/);
	    }
	    previousParent = currentParent;
	  }
	  
	  // Test
	  
		var html = "<div id='search_preview_close'><span id='preview_close_button'>close x</span></div>";
		
    	html += "<img src=" + (person.picture != null && person.picture != '' ? "/uploads/avatars/profile/"
				+ person.picture : "\"/opendirectory/img/default_gravatar.png\"") + " title=\"" +
				person.name + "\" height=\"106.5\" width=\"82.5\" id=\"search_preview_image\">"
		html += "<div id='search_preview_inner_wrapper'>";
		html += writeField("h3","search_preview_name",person.name);
		html += writeField("span","search_preview_title",person.title);
		html += writeField("span","search_preview_office",person.department);
		html += writeField("span","senate_location",person.location);
		html += writeField("span","senate_email",person.email);
		html += writeField("span","additional_email",person.email2);
		html += writeField("span","senate_phone",person.phone);
		html += writeField("span","additional_phone",person.phone2);
		html += "</div><br />";
		html += "<div id='search_preview_url'><a href=\"/opendirectory/person/" + person.uid + "/profile\">view full profile →</a></div>";
		
		$('.search_preview').html(html);
		$('.search_preview').css('display','block');
//		$('.search_preview').animate({ opacity: 'show' }, 500);
		
		
		$("#preview_close_button").click(function() {
//  	  $('.search_preview').animate({ opacity: 'hide' }, 500);
      $('.search_preview').css('display','none');
  	  currentParent.css('background', '#fff');
  	});
	});
	
	writeField = (function(type, id, data) {
		if(data !=  null && data != "") {
			return "<" + type + " id=\"" + id + "\">" + data + "</" + type + "><br/>"
		}
		return "";
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

