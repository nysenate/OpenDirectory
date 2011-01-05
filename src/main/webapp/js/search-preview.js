$(document).ready(function() {
  $(".search_name").click(function() {
    alert( event.target.id.split("_")[1] );
  });
});

