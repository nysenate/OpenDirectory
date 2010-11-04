package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.solr.Solr;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	
	private Solr solrServer;
	
	public UserServlet() {
		//Only executed on startup
		solrServer = new Solr().connect();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `opendirectory` away
	    tokens.nextToken(); //Throw `user` away
	    
	    if(tokens.hasMoreTokens()) {
	    	String command = tokens.nextToken();
	    	
	    	if(command.equals("login")) {
	    		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    	} else if (command.equals("logout")) {
	    		//Do different stuff
	    	}
	    }
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `opendirectory` away
	    tokens.nextToken(); //Throw `user` away
	    
	    if(tokens.hasMoreTokens()) {
	    	String command = tokens.nextToken();
	    	
	    	if(command.equals("login")) {
    			//Authenticate
	    		String cred = (String)request.getAttribute("name");
	    		String pass = (String)request.getAttribute("password");
	    		
	    		try {
	    			//Try to connect
	    			if (Ldap.authenticate(cred,pass)) {
	    				//Add some stuff to the cookie....
	    			} else {
	    				//Bad Authentication
		    			request.setAttribute("errorMessage", "Username and/or password were incorrect.");
		    			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    			}
	    			
	    		} catch (NamingException e) {
	    			//LDAP is down or something?
	    			request.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
	    			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    		}
	    		
	    	} else {
	    		//Bad command?
	    	}
	    	
	    	
	    }
	}
	
}
