function validate() {
	message = "";
	
	email = $('input[name=email2]').val();
	phone = $('input[name=phone2]').val();
	irc = $('input[name=irc]').val();
	linkedin = $('input[name=linkedin]').val();
	facebook = $('input[name=facebook]').val();
	twitter = $('input[name=twitter]').val();
	
	message += doCheck(email, /.*?@.*?\..*/,
			"<br/>Entered a valid email address");
	message += doCheck(phone,/\(\d{3}\)([ -])?\d{3}-\d{4}/,
			"<br/>Used (###) ###-#### for your phone number");
	message += doCheck(irc,/[A-Za-z\d\.\-_]+/,
			"<br/>Your irc name should only contain numbers, characters or punctuation");
	message += doCheck(twitter,/(http:\/\/)?(www\.)?twitter\.com\/[A-Za-z\d\.-_]/i,
			"<br/>Used the format twitter.com/your-user-name for your Twitter URL");
	message += doCheck(facebook,/(http:\/\/)?(www\.)?facebook\.com\/.*/i,
			"<br/>Used the format facebook.com/your-user-name for your Facebook URL");
	message += doCheck(linkedin,/(http:\/\/)?(www\.)?linkedin\.com\/.*/i,
			"<br/>Used the format linkedin.com/your-user-anem for your LinkedIn URL");
	
	return message;
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
			$('#edit_error').html("It looks like there may be a problem with one of your contact<br/>" +
									"fields.  Please check to make sure you:<br/>" + msg);
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