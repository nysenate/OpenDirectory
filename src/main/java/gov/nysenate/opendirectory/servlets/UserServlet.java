package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
//import gov.nysenate.opendirectory.solr.Solr;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	
	//private Solr solrServer;
	
	public UserServlet() {
		//Only executed on startup
		//solrServer = new Solr().connect();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Get Request");
		HttpSession session = request.getSession(false);
		
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `opendirectory` away
	    tokens.nextToken(); //Throw `user` away
	    
	    
	    if(tokens.hasMoreTokens()) {
	    	String command = tokens.nextToken();
	    	if(command.equals("login")) {
	    		System.out.println("User/Login get request");
	    		if(session == null || session.getAttribute("uid") == null) {
	    			System.out.println("Not yet logged in");
	    			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	    		} else {
	    			System.out.println("Already logged in");	    			
	    			response.sendRedirect("/opendirectory/person/"+session.getAttribute("uid"));
	    		}
	    		
	    	} else if (command.equals("logout")) {
	    		System.out.println("User/Logout get request");
	    		//Do different stuff
	    		if(session != null) {
    				System.out.println("Logging user out");
	    			session.removeAttribute("uid");
	    		} else
	    			System.out.println("User not logged in");
	    		
	    		response.sendRedirect("/opendirectory/");
	    	}
	    }
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post request");
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `opendirectory` away
	    tokens.nextToken(); //Throw `user` away
	    
	    if(tokens.hasMoreTokens()) {
	    	String command = tokens.nextToken();
	    	
	    	if(command.equals("login")) {
	    		System.out.println("User/Login post request");
    			//Authenticate
	    		String cred = (String)request.getParameter("name");
	    		String pass = (String)request.getParameter("password");
	    		HttpSession session = request.getSession();
	    		
	    		//Redirect if they are already logged in
	    		if( session != null && session.getAttribute("uid") != null ) {
	    			System.out.println("User Already logged in");
	    			response.sendRedirect("/opendirectory/person/"+session.getAttribute("uid"));
	    		}
	    		else {
	    			System.out.println("User not yet logged in");
		    		try {
		    			//Try to connect
		    			if (Ldap.authenticate(cred,pass)) {
		    				System.out.println("Authentication Success");
		    				//Add some stuff to the cookie....
		    				HttpSession newSession = request.getSession(true);
		    				newSession.setAttribute("uid",cred);
		    				response.sendRedirect("/opendirectory/person/"+session.getAttribute("uid"));
		    			} else {
		    				System.out.println("Authentication failure");
		    				//Bad Authentication
			    			request.setAttribute("errorMessage", "Username and/or password were incorrect.");
			    			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		    			}
		    			
		    		} catch (NamingException e) {
		    			//LDAP is down or something?
		    			e.printStackTrace();
		    			request.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
		    			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		    		}
	    		}
	    		
	    	} else {
	    		response.sendRedirect("/opendirectory/");
	    	}
	    	
	    	
	    }
	}
}
