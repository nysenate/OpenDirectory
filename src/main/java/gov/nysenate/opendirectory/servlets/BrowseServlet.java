package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
 
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@SuppressWarnings("serial")
public class BrowseServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `OpenDirectory` away
	    tokens.nextToken(); //Throw `browse` away
	    if (tokens.hasMoreTokens()) {
	    	String name = tokens.nextToken().toLowerCase();
	    	if ( name.equals("department")) {
	    		getServletContext().getRequestDispatcher("/dept.jsp").forward(request, response);
	    	} else if ( name.equals("firstname") ) {
	    		try {
		    		Ldap ldap = new Ldap().connect();
		    		ArrayList<Person> people = (ArrayList<Person>)ldap.getPeople();
		    		
		    		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		    		HashMap<String,ArrayList<Person>> data = new HashMap<String,ArrayList<Person>>();
		    		
		    		for(int i = 0; i<alphabet.length(); i++ ) {
		    			data.put(String.valueOf(alphabet.charAt(i)).toUpperCase(), new ArrayList<Person>());
		    		}
		    		for(Person p : people) {
		    			if(p.getFirstName()!=null && p.getFirstName().length()!=0) {
		    				data.get(String.valueOf(p.getFirstName().charAt(0)).toUpperCase()).add(p);
		    			}
		    			//else
		    			//	System.out.println(p); System.out.println();
		    		}
		    		for(ArrayList<Person> peeps : data.values()) {
		    			Collections.sort(peeps, new Comparator<Person>(){
		    				public int compare(Person a, Person b) {
		    					return a.getFirstName().compareToIgnoreCase(b.getFirstName());
		    				}
		    			});
		    		}
		    		request.setAttribute("people", data);
		    		getServletContext().getRequestDispatcher("/first.jsp").forward(request, response);
	    		} catch (NamingException e) {
	    			System.out.println(e);
	    		}
	    	} else if ( name.equals("lastname") ) {
	    		getServletContext().getRequestDispatcher("/last.jsp").forward(request, response);
	    	} else if ( name.equals("location") ) {
	    		getServletContext().getRequestDispatcher("/loc.jsp").forward(request, response);
	    	}
	    } else {
	    	getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	    }
	}
}