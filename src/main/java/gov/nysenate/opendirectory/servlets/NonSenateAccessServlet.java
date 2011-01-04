package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



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
		self.render("access.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String command = urls.getCommand(request);
	    
	    try {
		    //If they are posting to a bad place, 
		    if(command == null)
		    	throw new ExternalServletException("No command supplied to POST request.");
		    
	    	if(command.equals("login")) {
	    		doLogin(self);
	    		
	    	}
		} catch (ExternalServletException e) {
	    	doException(self,e);
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
	    			self.httpRequest.setAttribute("errorMessage", "Username and/or password were incorrect.");
	    			self.render("/access.jsp");
    			}
    			
    		//There is a small chance that a connection to LDAP is not available for some reason
    		} catch (NamingException e) {
    			
    			//Record the Error to our logs
    			e.printStackTrace();
    			
    			//Alert the user to the situation
    			self.httpRequest.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
    			self.render("access.jsp");
    		}
		
		//Person is not in our database
		} else {
			self.httpRequest.setAttribute("errorMessage", "The user you have specified `"+cred+"` is not indexed in OpenDirectory.");
			self.render("access.jsp");
		}

	}
}
