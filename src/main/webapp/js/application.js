function validate() {
	message = "";
	
	email = $('input[name=email2]').val();
	phone = $('input[name=phone2]').val();
	irc = $('input[name=irc]').val();
	linkedin = $('input[name=linkedin]').val();
	facebook = $('input[name=facebook]').val();
	twitter = $('input[name=twitter]').val();
	
	message += doCheck(email, /.*?@.*?\..*/,
			"<br/>Enter a valid email addres");
	message += doCheck(phone,/\(\d{3}\)([ -])?\d{3}-\d{4}/,
			"<br/>Use (###) ###-#### for your phone number");
	message += doCheck(irc,/[A-Za-z\d\.\-_]+/,
			"<br/>Your irc name should only contain numbers, characters or punctuation");
	message += doCheck(twitter,/(http:\/\/)?(www\.)?twitter\.com\/[A-Za-z\d\.-_]/i,
			"<br/>Your Twitter URL should look like twitter.com/your-user-name");
	message += doCheck(facebook,/(http:\/\/)?(www\.)?facebook\.com\/.*/i,
			"<br/>Your Facebook URL should look like facebook.com/your-user-name");
	message += doCheck(linkedin,/(http:\/\/)?(www\.)?linkedin\.com\/.*/i,
			"<br/>Provide a proper link to your LinkedIn profile");
	
	return message.replace(/^<br\/>/,"");
}


function doCheck(str, regex, message) {
	if(str != null & str != "") {
		if(!str.match(regex)) {
			return message;
		}
	}
	return "";
}

$(document).ready( function() {
	$('button[name=submit_changes]').click(function() {
		msg = validate();
		if(msg == "") {
			return true;
		}
		else {
			$('#edit_button').hide();
			$('#edit_error').html(msg);
			$('#edit_error').css({'display':'inherit'});
			$('html,body').animate({
				scrollTop:$('html').offset().top}, 500);
			return false;
		}
	});
	$(".entity_button").each( function() {
		var button = $(this);
		var name = button.attr('id').split('_')[1];
		var list = $("#list_"+name);
		
		$(this).toggle(
			function() {
				list.hide();
				button.html("+");
				return false;
			},
			function() {
				list.show();
				button.html("-");
				return false;
			}
		).click();
		
	});
	
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