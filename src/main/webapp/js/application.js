$(document).ready( function() {
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