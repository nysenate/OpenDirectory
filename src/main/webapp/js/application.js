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
	
	var delay = (function(){
		var timer = 0;
		return function(callback, ms){
			clearTimeout (timer);
			timer = setTimeout(callback, ms);
		};
	})();
	
	var writeQuickResult = function(elem, input) {
		if(input != '') {
			$(elem).css('visibility','visible');
			$(elem).html("");
			
			
			var queryTerm = "query=" + input;
			var query = "/opendirectory/api/1.0/search/xml?" + queryTerm;
			
			$.get(query, function(data) {
				var html = "";
				
				var total = $(data).find("total").html();
				
				html ='<li><em>' + total + ' total results... (<a href="' + query + '">view all</a>)</em></li>';
				
				$(data).find('person:lt(10)').each(function() {
					var fName = $(this).find('firstName').html();
					var lName = $(this).find('lastName').html();
					var dept = $(this).find('department').html();
					var uid = $(this).find('uid').html();
					
					html += '<li class="quickresult_box"><a href="/opendirectory/person/' + uid + '/profile" class="sublink">';
					html += fName + ' ' + lName + ' - ' + dept;
					html += '</a></li>';
				});
				
				$(elem).html(html);
				
			});
		}
		else {
			$(elem).css('visibility','hidden');
		}
	};
	
	$('#nav_search_input').keyup(function() {
		//writeQuickResult($('#quickresult-header'), this);
	});
	
	$('#s').keyup(function() {
		input = $(this).val();
		delay(function() {
			writeQuickResult($('#quickresult-body'), input);
		},250);
	});
	
	
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
	$('input[name=phone2]').focus(function() {
		if($(this).val() == '(###) ###-####') {
			$(this).val('');
		}
	});
	
	$('input[name=phone2]').blur(function() {
		if($(this).val() == '') {
			$(this).val('(###) ###-####');
		}
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