$(document).ready( function() {
	$(".entity_button").each( function() {
		var button = $(this);
		var name = button.attr('id').split('_')[1];
		var list = $("#list_"+name);
		
		$(this).toggle(
			function() {
				list.hide();
				return false;
			},
			function() {
				list.show();
				return false;
			}
		).click();
		
	});
});