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
	if(phone != '(###) ###-####') {
		message += doCheck(phone,/\(\d{3}\)([ -])?\d{3}-\d{4}/,
		"<br/>Used (###) ###-#### for your phone number");
	}
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
	if(str != null & !str.match(/[ ]*/)) {
		if(!str.match(regex)) {
			return message;
		}
	}
	return "";
}

var skill_set, interest_set;
var type_index;

$(document).ready( function() {
	$('.suggestions').css('visibility','hidden');
	
	/* for profile editing */
	
	repopulateTextArea = function(list, name, index) {
		list[type_index] = $('.suggestions_box:eq(' + index + ')').html();
		var n = "";
		for(x in list) {
			if(x == 0) {
				n += list[x];
			}
			else {
				n += ", " + list[x];
			}
		}
		$('textarea[name=' + name + ']').val(n);
		return list;
	};
	
	var cleanSet = function(text) {
		var temp = text.split(',');	
		for(x in temp) {
			temp[x] = temp[x].replace(/^\s*|\s*$/g,'');
		}
		return temp;
	};
	
	var getUniqueEntry = function(list,val) {
		for(x in list) {
			if(list[x] == val) {
				return false;
			}
		}
		return true;
	};
	
	getTypeSimilarSet = function(type, xml_type, type_set, type_text) {
		var type_results = new Array();
		
		var new_type_set = cleanSet(type_text);
		
		for(x in new_type_set) {
			if(type_set[x] == null || type_set[x] != new_type_set[x]) {
				type_index = x;
				type_set = new_type_set;
				
				var type_query_word = new_type_set[x];
				
				var query = "/opendirectory/api/1.0/search/xml?query=" + type + ":(" + type_query_word + "~) OR "
						+ type + ":(" + type_query_word + "*) OR " + type + ":(" + type_query_word + ")";
				
				 $.get(query, function(data) {
					
					$(data).find('person').each(function() {
						
						$(this).find(xml_type).each(function() {
							
							if($(this).html().toLowerCase().indexOf(type_query_word.toLowerCase()) == 0) {										
								if(getUniqueEntry(type_results,$(this).html()) 
										&& getUniqueEntry(type_set, $(this).html())) {
									type_results[type_results.length] = $(this).html();
								}
							}
							
							return (type_results.length != 5);
						});
					});
					var html = "";
					
					for(x in type_results) {
						var result = type_results[x];
						if(x == 0) {
							html += '<li id="selected_suggestion" class="suggestions_box">';
						}
						else {
							html += '<li class="suggestions_box">';
						}
						html += result;
						html += '</li>';
					}
					
					$('#' + type + "_suggestions").css('visibility','visible');
					$('#' + type + "_suggestions").html(html)
				});
				break;
			}
		}
		return type_set;
	};
	
	$('textarea[name=interests]').keyup(function() {
		var interest_text = $(this).val();
		if(interest_set == null) {
			interest_set = cleanSet(interest_text);
		}
		delay(function() {
			interest_set = getTypeSimilarSet("interests","interest",interest_set, interest_text);
		},250);
	});
	
	$('textarea[name=skills]').keyup(function() {
		var skill_text = $(this).val();
		if(skill_set == null) {
			skill_set = cleanSet(skill_text);
		}
		delay(function() {
			skill_set = getTypeSimilarSet("skills","skill",skill_set, skill_text);
		},250);
	});
	
	$('.edit_left').click(function(e) {
		if ($(e.target).is('.suggestions_box')) {
			var index = $(e.target).index();
			skill_set[type_index] = $('.suggestions_box:eq(' + index + ')').html();
			skill_set = repopulateTextArea(skill_set,"skills",index);
			$('.suggestions').css('visibility','hidden');
		}
	});
	
	$('.edit_right').click(function(e) {
		if ($(e.target).is('.suggestions_box')) {
			var index = $(e.target).index();
			interest_set[type_index] = $('.suggestions_box:eq(' + index + ')').html();
			interest_set = repopulateTextArea(interest_set,"interests",index);
			$('.suggestions').css('visibility','hidden');
		}
	});
	
	$('body').click(function(e) {
		if(!$(e.target).is('.edit_left') && !$(e.target).is('.edit_right')) {
			$('.suggestions').css('visibility','hidden');
		}
	});
	
	$('textarea[name=skills],textarea[name=interests]').keydown(function(e) {
		var type = $(this).attr('name');
		var key_code = e.keyCode;
		var visible = $('#' + type + '_suggestions').css('visibility') == 'visible' ? true:false;
		
		if(visible) {
			var index = $('#selected_suggestion').index();
			//up
			if(key_code == 38) {
				if(index > 0) {
					$('.suggestions_box:eq(' + index + ')').attr('id','');
					$('.suggestions_box:eq(' + (index - 1) + ')').attr('id','selected_suggestion');
				}
				return false;
			}
			//down
			else if(key_code == 40) {
				var end = $('.suggestions_box').length - 1;
				if(index < end) {
					$('.suggestions_box:eq(' + index + ')').attr('id','');
					$('.suggestions_box:eq(' + (index + 1) + ')').attr('id','selected_suggestion');
				}
				return false;
			}
			//enter
			else if(key_code == 13) {
				if(type == "skills") {
					skill_set[type_index] = $('.suggestions_box:eq(' + index + ')').html();
					skill_set = repopulateTextArea(skill_set,type,index);
				}
				else {
					interest_set[type_index] = $('.suggestions_box:eq(' + index + ')').html();
					interest_set = repopulateTextArea(interest_set,type,index);
				}
				$('.suggestions').css('visibility','hidden');
				return false;
			}
		}
	});
	
	/*$('.vcard').qtip({
		content: 'vCards are electronic business cards. They can be placed into an address book program on your computer or phone and will add that contact with any information on the card. vCard information can include text as well as photos and audio.',
		position: {
			corner: {
				target: 'bottomMiddle',
				tooltip:'topleft'
			}
		},
		style: {
			name: 'blue'
		}
	});*/
	
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
	
	/* for partial search results */
	
	$('#search_secondary').hide();
	$('#show_search_secondary').click(function() {
		$('#search_secondary').slideToggle(250, function() {
			$('#show_search_secondary').html("<h2>Click here to " + ($('#search_secondary').is(":visible") ? "hide":"show") + " partial matches</h2>")

		});
	});
	
	/* for quick search */
	
	var delay = (function(){
		var timer = 0;
		return function(callback, ms){
			clearTimeout (timer);
			timer = setTimeout(callback, ms);
		};
	})();
	
	$('#nav_search_input').keyup(function() {
		input = $(this).val();
		delay(function() {
			initQuickResult(input, doHeaderQuickResult, $('#quickresult-header'));
		},250);
	});
	
	$('#s').keyup(function() {
		input = $(this).val();
		delay(function() {
			initQuickResult(input, doIndexQuickResult, $('#quickresult-body'));
		},250);
	});
	
	doHeaderQuickResult = (function(input, data, elem) {
		var html = "";
		var total = $(data).find("total").html();
		
		html ='<li><em>' + total + ' total results... (<a href="/opendirectory/search/?query=' 
			+ input + '">view all</a>)</em></li>';
		$(data).find('person:lt(10)').each(function() {
			var fName = $(this).find('firstName').html();
			var lName = $(this).find('lastName').html();
			var dept = $(this).find('department').html();
			var uid = $(this).find('uid').html();
			
			html += '<li class="quickresult_box"><a href="/opendirectory/person/' 
				+ uid + '/profile" class="sublink">';
			html += fName + ' ' + lName + ' - ' + dept;
			html += '</a></li>';
		});
		$(elem).html(html);
		
		/*var html = "";
		var total = $(data).find("total").html();
		
		html ='<li><em>' + total + ' total results... (<a href="/opendirectory/search/?query=' 
			+ input + '">view all</a>)</em></li>';
		$(data).find('person:lt(10)').each(function() {
			var fName = $(this).find('firstName').html();
			var lName = $(this).find('lastName').html();
			var uid = $(this).find('uid').html();
			
			html += '<li class="quickresult_box"><a href="/opendirectory/person/' 
				+ uid + '/profile" class="sublink">';
			html += fName + ' ' + lName;
			html += '</a></li>';
		});
		$(elem).html(html);*/
	});
	
	doIndexQuickResult = (function(input, data, elem) {
		var html = "";
		var total = $(data).find("total").html();
		
		html ='<li><em>' + total + ' total results... (<a href="/opendirectory/search/?query=' 
			+ input + '">view all</a>)</em></li>';
		$(data).find('person:lt(10)').each(function() {
			var fName = $(this).find('firstName').html();
			var lName = $(this).find('lastName').html();
			var dept = $(this).find('department').html();
			var uid = $(this).find('uid').html();
			
			html += '<li class="quickresult_box"><a href="/opendirectory/person/' 
				+ uid + '/profile" class="sublink">';
			html += fName + ' ' + lName + ' - ' + dept;
			html += '</a></li>';
		});
		$(elem).html(html);
	});
	
	initQuickResult = (function(input, callback, elem) {
		if(input != '') {
			$(elem).css('visibility','visible');
			$(elem).html("");
			
			var queryTerm = "query=" + input;
			var query = "/opendirectory/api/1.0/search/xml?" + queryTerm;
			doSearch(input, query, callback, elem);
		}
		else {
			$(elem).css('visibility','hidden');
		}	
	});
	
	doSearch = (function(input, uri, callback, elem) {
		$.get(uri, function(data) {
			callback(input, data, elem);
		});
	});
	
	/* for browse by */
	
	$(".button_div").each( function() {
		var button = $(this).find('.entity_button');
		var name = button.attr('id').split('_')[1];
		var list = $("#list_"+name);
		
		$(this).toggle(
			function() {
				list.hide();
				button.html("<img src='../img/interface_open.png' class='ui_list_button'>");
				return false;
			},
			function() {
				list.show();
				button.html("<img src='../img/interface_close.png' class='ui_list_button'>");
				return false;
			}
		).click();
		
	});
});