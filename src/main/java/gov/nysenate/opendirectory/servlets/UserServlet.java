package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		String command = urls.getCommand(request);
	    if(command != null) {
	    	if(command.equals("login")) {
	    		String uid = (String)self.httpSession.getAttribute("uid"); 
	    		if( uid == null) {
	    			self.render("/login.jsp");
	    		} else {	    			
	    			self.redirect(urls.url("person",uid));
	    		}
	    		
	    	} else if (command.equals("logout")) {
    			self.httpSession.removeAttribute("uid");
	    		
	    		self.redirect(urls.url("index"));
	    	}
	    }
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
	    String command = urls.getCommand(request);
	    if(command != null) {
	    	if(command.equals("login")) {
	    		String cred = (String)request.getParameter("name");
	    		String pass = (String)request.getParameter("password");
	    		
	    		//Check to see if they are already logged in
	    		if(self.httpSession.getAttribute("uid") != null ) {
	    			self.redirect(urls.url("person",cred));
	    			
    			// Check to see if they are in our database
	    		} else if (self.solrSession.loadPersonByUid(cred) != null) {
		    		try {
		    			//Try to connect
		    			if ( (cred.equalsIgnoreCase("opendirectory") && pass.equals("senbook2010")) || Ldap.authenticate(cred,pass)) {
		    				self.httpSession.setAttribute("uid",cred);
		    				self.redirect(urls.url("person",cred));
		    			} else {
			    			request.setAttribute("errorMessage", "Username and/or password were incorrect.");
			    			self.render("/login.jsp");
		    			}
		    			
		    		} catch (NamingException e) {
		    			//LDAP is down or something?
		    			e.printStackTrace();
		    			request.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
		    			self.render("/login.jsp");
		    		}
	    		
	    		//Person is not in our database
	    		} else {
	    			request.setAttribute("errorMessage", "The user you have specified `"+cred+"` is invalid for the system.");
	    			self.render("/login.jsp");
	    		}
	    	} else {
	    		self.redirect(urls.url("index"));
	    	}
	    }
	}
}