package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.ExternalPerson;
import gov.nysenate.opendirectory.utils.Mailer;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;



public class NonSenateAccessServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	public class ExternalServletException extends Exception {
		private static final long serialVersionUID = 1L;
		public ExternalServletException(String m) { super(m); }
		public ExternalServletException(String m, Throwable t) { super(m,t); }
	}
       
    public NonSenateAccessServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		String command = urls.getCommand(request);
		
		try {
			if(command == null) {
				self.render("external/index.jsp");
			}
			else if(command.equals("login")) {
				self.render("external/login.jsp");
			}
			else if(command.equals("logout")) {
				self.httpSession.removeAttribute("externalPerson");
				self.redirect(urls.url("external"));
			}
			else if(command.equals("register")) {
				self.render("external/register.jsp");
			}
			else if(command.equals("auth")) {
				doExternalAuth(self);
			}
	    	else throw new ExternalServletException("Invalid command `"+command+"` supplied.");
		} catch (ExternalServletException e) {
			doException(self,e);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String command = urls.getCommand(request);
	    
	    try {
		    if(command == null){
		    	doLogin(self);
		    }
		    else if(command.equals("login")) {
	    		doExternalLogin(self);
	    	}
	    	else if(command.equals("register")) {
	    		doExternalRegister(self);
	    	}
	    	else if(command.equals("auth")) {
	    		self.httpResponse.getWriter().println("hey");//doExternalAuth(self);
	    	}
	    	else throw new ExternalServletException("Invalid command `"+command+"` supplied.");
		} catch (ExternalServletException e) {
	    	doException(self,e);
	    } catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	    
	private void doExternalRegister(Request self) throws IOException, ServletException, SolrServerException {
		String firstName = (String)self.httpRequest.getParameter("firstName");
		String lastName = (String)self.httpRequest.getParameter("lastName");
		String email1 = (String)self.httpRequest.getParameter("email1");
		String email2 = (String)self.httpRequest.getParameter("email2");
		String password1 = (String)self.httpRequest.getParameter("pword1");
		String password2 = (String)self.httpRequest.getParameter("pword2");
		String phone = (String)self.httpRequest.getParameter("phone2");
		
		String error = "";
		if(firstName == null || firstName.equals("")) {
			error += "<br/>Entered your first name.";
		}
		if(lastName == null || lastName.equals("")) {
			error += "<br/>Entered your last name.";
		}
		if(phone.equals("(###) ###-####")) {
			error += "<br/>Entered a phone number";
		}
		else {
			if(!phone.matches("\\(\\d{3}\\)[ \\-]?\\d{3}\\-\\d{4}")) {
				error += "<br/>Used (###) ###-#### for your phone number format";
			}
		}
		if(password1.length() < 8) {
			error += "<br/>Entered a password at least 8 characters long";
		}
		else {
			if(!password1.equals(password2)) {
				error += "<br/>Entered matching passwords";
			}
		}
//		if(email1 == null || email1.equals("") || !email1.matches(".+?@(.*+\\.state\\.ny\\.us|ny\\.gov)")) {
//			error += "<br/>Entered a valid email address (ending in state.ny.us or ny.gov)";
//		}
//		else {
//			if(email2 == null || !email1.equals(email2)) {
//				
//			}
//			else {
//				ExternalPerson person = self.solrSession.loadExternalPersonByEmail(email1);
//				if(person != null) {
//					if(person.getAuthorized()) {
//						error = "<br/>That email address already exists on OpenDirectory.  If you need help " +
//								"retrieving your password please " +
//								"<a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
//					}
//					else {
//						error = "<br/>That email address already exists on OpenDirectory, so we've dispatched " +
//								"another activation email.  If you do not receive the email or have any questions " +
//								"please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
//						Mailer.sendExternalAuthorizationMail(person);
//					}
//				}
//			}
//		}
		
		
		if(error.equals("")) {
			ExternalPerson person = new ExternalPerson(firstName, lastName, email1, phone);
			person.setAuthorized(false);
			person.encryptPassword(password1);
			person.setAuthorizationHash();
			
			Mailer.sendExternalAuthorizationMail(person);
			
			self.solrSession.saveExternalPerson(person);
			
			self.httpRequest.setAttribute("header", "Success!");
			self.httpRequest.setAttribute("message", "You will receive an email shortly to activate your account, " +
					"until then you will not be able to log in.  If you do not receive an email or have any questions " +
					"please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.");
    		self.render("external/message.jsp");
		}
		else {
			self.httpRequest.setAttribute("phone2", phone);
			self.httpRequest.setAttribute("email", email1);
			self.httpRequest.setAttribute("firstName", firstName);
			self.httpRequest.setAttribute("lastName", lastName);

			self.httpRequest.setAttribute("error", error);
    		self.render("external/register.jsp");

		}
		
	}

	private void doExternalAuth(Request self) throws SolrServerException, IOException, ServletException {
		String email = ((String)self.httpRequest.getParameter("email"));
		String key = (String)self.httpRequest.getParameter("key");
		
		if(email == null || key == null) {
			populateMessage(self, MessageCode.CREDS_NOT_PROVIDED);
		}
		
		ExternalPerson person = self.solrSession.loadExternalPersonByEmail(email);
		
		if(person == null) {
			//couldn't find person
			populateMessage(self, MessageCode.PERSON_NOT_FOUND);
		}
		else {
			if(person.getAuthorized()) {
				//no need
				populateMessage(self, MessageCode.ALREADY_AUTHORIZED);
			}
			else {
				if(person.getAuthorizationHash().equals(key)) {
					person.setAuthorizationHash("");
					person.setAuthorized(true);
					self.solrSession.saveExternalPerson(person);
					populateMessage(self, MessageCode.AUTH_SUCCESS);
				}
				else {
					//bad hash
					person.setAuthorizationHash();
					Mailer.sendExternalAuthorizationMail(person);
					populateMessage(self, MessageCode.AUTH_FAILURE);
				}
			}
		}
		self.render("external/message.jsp");
	}
	
	public enum MessageCode {
		CREDS_NOT_PROVIDED, PERSON_NOT_FOUND, ALREADY_AUTHORIZED, AUTH_SUCCESS, AUTH_FAILURE, NOT_ACTIVATED
	}
	
	public void populateMessage(Request self, MessageCode code) {
		String header = null;
		String error = null;
		String message = null;
		switch(code) {
			case CREDS_NOT_PROVIDED:
				header = "Error";
				error = "<br/>Invalid credentials specified, if you think this is an error" +
					"please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
				break;
			case PERSON_NOT_FOUND:
				header = "Error";
				error = "<br/>We could not find this person.  Are you sure you've " +
						"<a href=\"" + urls.url("external","register") + "\">registered</a>?" + 
						"  If you think this is an error please " +
						"<a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
				break;
			case ALREADY_AUTHORIZED:
				header = "Error";
				error = "<br/>This person is already authorized, if you think this is an error" +
					"please <a href=\"http://www.nysenate.gov/contact\">contact us</a>." +
					"  Or click <a href=\"" + urls.url("external","login") + "\">here</a> to login.";
				break;
			case AUTH_SUCCESS:
				header = "Success";
				message = "<br/>Your account has been activated.  You can click " +
					"<a href=\"" + urls.url("external","login") + "\">here</a> to login.";
				break;
			case AUTH_FAILURE:
				header = "Error";
				error = "<br/>There was an error authorizing your account, so we've dispatched " +
					"another activation email.  If you do not receive the email or have any questions " +
					"please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
				break;
			case NOT_ACTIVATED:
				header = "Error";
				error = "<br/>Your account must be activated before you can log in.  You should have " + 
					"received an email when you first reigstered which containts an activation link, " + 
					"if not we've dispatched another activation email.  If you do not receive the email " +
					"or have any questions please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.";
				break;
		}
		self.httpRequest.setAttribute("header", header);
		self.httpRequest.setAttribute("error", error);
		self.httpRequest.setAttribute("message", message);
	}

	private void doExternalLogin(Request self) throws IOException, ServletException {
		String cred = ((String)self.httpRequest.getParameter("name"));
		String pass = (String)self.httpRequest.getParameter("password");
		ExternalPerson person = self.solrSession.loadExternalPersonByEmail(cred);
		
		//check login correct
		if(person != null && pass != null && person.checkPassword(pass)) {
			if(person.getAuthorized()) {
				self.httpSession.setAttribute("externalPerson", person.getFirstName());
				self.httpSession.setAttribute("externalUid", person.getEmail());
				self.redirect(urls.url("index"));
			}
			else {
				populateMessage(self, MessageCode.NOT_ACTIVATED);
				Mailer.sendExternalAuthorizationMail(person);
				self.render("external/message.jsp");
			}
			
		}
		else {
			self.httpRequest.setAttribute("errorMessage", "Username and/or password were incorrect.  Are you sure you've <a href=\"" + urls.url("external","register") + "\">registered?</a>");
			self.render("external/login.jsp");
		}
	}

	public void doException(Request self, ExternalServletException e) throws ServletException, IOException {
		System.out.println(e.getMessage());
		e.printStackTrace();
		if(e.getCause()!=null)
			e.getCause().printStackTrace();
		self.redirect(urls.url("external"));
	}
	
	public void doLogin(Request self) throws ExternalServletException, ServletException, IOException {
		String cred = ((String)self.httpRequest.getParameter("name")).toLowerCase();
		String pass = (String)self.httpRequest.getParameter("password");
		
		//If they are already logged in, forward to their profile
		if(self.httpSession.getAttribute("uid") != null ) {
			self.redirect(urls.url("person",cred));
			
		// Check to see the user attempting to log in is valid before authenticating
		// This needs to be done because not all authenticating users are in the OpenDirectory
		} else if (self.solrSession.loadPersonByUid(cred) != null) {
    		try {
    			
    			//Attempt to authenticate and login the user with the credentials supplied
    			//Make temporary exceptions for non LDAP records (for testing)
    			if ( /*(cred.equalsIgnoreCase("opendirectory") && pass.equals("senbook2010"))
    					|| (cred.equalsIgnoreCase("graylin") && pass.equals("graylin1"))
    					|| */ Ldap.authenticate(cred,pass)) {
    				self.httpSession.setAttribute("uid",cred);
    				self.redirect(urls.url("person",cred,"profile"));
    				
    			//Otherwise, alert them that their combination was wrong and redirect them to try again
    			} else {
	    			self.httpRequest.setAttribute("errorMessage", "Username and/or password are incorrect.");
	    			self.render("external/index.jsp");
    			}
    			
    		//There is a small chance that a connection to LDAP is not available for some reason
    		} catch (NamingException e) {
    			
    			//Record the Error to our logs
    			e.printStackTrace();
    			
    			//Alert the user to the situation
    			self.httpRequest.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
    			self.render("external/index.jsp");
    		}
		
		//Person is not in our database
		} else {
			self.httpRequest.setAttribute("errorMessage", "Username and/or password were incorrect.");
			self.render("external/index.jsp");
		}

	}
}
