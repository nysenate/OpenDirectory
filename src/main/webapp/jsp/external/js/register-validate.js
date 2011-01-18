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
      rules: {
        email: {
          required: true,
          regex: /.*?@(.*\.state\.ny\.us|ny\.gov)/
//                email_validate: true
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
      messages: {
        email: "<p class='register_error'>Please enter your email</p>",
        email_verify: "<p class='register_error'>Email addresses must match</p>",
        firstname: "<p class='register_error'>First Name is Required</p>",
        lastname: "<p class='register_error'>Last Name is Required</p>",
        phone: "<p class='register_error'>Phone is Required. Format (###) ###-####</p>",
        password_verify: "<p class='register_error'>Passwords must match</p>"
      }
      
    });
    
    

});


