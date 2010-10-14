package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;

import java.io.IOException;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PersonServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		    StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		    tokens.nextToken(); //Throw `OpenDirectory` away
		    tokens.nextToken(); //Throw `person` away
		    if(tokens.hasMoreTokens()) {
		    	try {
			    	String uid = tokens.nextToken().toLowerCase();
			    	Collection<Person> people = new Ldap().connect().getPersonByUid(uid);
			    	request.setAttribute("person", people.iterator().next());
				    getServletContext().getRequestDispatcher("/profile.jsp").forward(request, response);
		    	} catch (NullPointerException e) {
		    		
		    	}
		    } else {
		    	
		    }
		} catch (NamingException e) {
			
		}
	}
}