$(document).ready( function() {
			
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
});
		