$(document).ready(function() {
//        alert("Hello World");
    
    $.validator.addMethod (
      "regex",
      function(value, element, regex) {
        return this.optional(element) || regex.test(value);
      },
      "Please check your input."
    );
    
    var validator = $('#external_registration').validate({
      // The rules for validation
      rules: {
        email: {
          required: true,
          regex: /.*?@(.*\.state\.ny\.us|ny\.gov)/
        },
        email_verify: {
          required: true,
          equalTo: '#email'
        },
        firstname: {
          required: true
        },
        lastname: {
          required: true
        },
        phone: {
          required: true,
          regex: /\(\d{3}\)([ -])?\d{3}-\d{4}/
        },
        password: {
          password: '#email',
          minlength: 8
        },
        password_verify: {
          required: true,
          equalTo: '#password'
        }
      },
      // The messages that will be displayed
      messages: {
        email: " New York State email required",
        email_verify: " Email addresses must match",
        firstname: " First Name is Required",
        lastname: " Last Name is Required",
        phone: " Phone required. Check formatting.",
        password_verify: " Passwords must match",
      },
      // Where to place the Errors (taking into accout the Table)
      errorPlacement: function(error, element) {
        if (element.attr('id') != 'password') {
          error.prependTo( element.parent().next() );
        }
  			
  			element.css('border', '1px red solid');
  		},
  		// Sets Success and adds the success thingie
  		success: function(label, element) {
//  		  label.parent().previous().children().css('border', 'none');
  		  label.html(" ").addClass("success");
        label.parent().prev().children(':first-child').css('border', 'none');
		  }
      
    });
    
    

});


