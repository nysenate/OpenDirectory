$(document).ready(function() {
   var validator = $('#external_registration').validate({
     rules: {
       firstname: "required",
       lastname: "required",
     },
     message: {
       firstname: "Enter your First Name",
       lastname: "Enter your Last Name",
     },
     errorPlacement: function(error, element) {
       error.appendTo(element.parent().next());
     },
   });
   
   $.validator.addMethod(
            "regex",
            function(value, element, regexp) {
                var check = false;
                var re = new RegExp(regexp);
                return this.optional(element) || re.test(value);
            },
            "Please check your input."
    );
   
   alert("Hello World");
   
}


