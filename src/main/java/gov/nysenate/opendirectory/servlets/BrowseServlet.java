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
	    	String type = tokens.nextToken().toLowerCase();
	    	if ( type.equals("department")) {
	    		BrowseByDepartment(request,response);
	    	} else if ( type.equals("firstname") ) {
    			BrowseByFirstName(request,response);
	    	} else if ( type.equals("lastname") ) {
	    		BrowseByLastName(request,response);
	    	} else if ( type.equals("location") ) {
	    		BrowseByLocation(request,response);
	    	}
	    } else {
	    	getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	    }
	}
	
	private void BrowseByDepartment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/dept.jsp").forward(request, response);
	}
	private void BrowseByLocation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/loc.jsp").forward(request, response);
	}
	private void BrowseByLastName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			long start = System.currentTimeMillis();
			ArrayList<Person> people = (ArrayList<Person>)new Ldap().connect().getPeople();
			long stop = System.currentTimeMillis();
			System.out.print("Retrieval in: "+(stop-start));
			
			start = System.currentTimeMillis();
    		HashMap<String,ArrayList<Person>> data = new HashMap<String,ArrayList<Person>>();
    		for(Person p : people) {
    			InsertPersonByString(p.getLastName(), p, data);
    		}
    		for(ArrayList<Person> peeps : data.values()) {
    			Collections.sort(peeps, new Person.ByLastName());
    		}
    		stop = System.currentTimeMillis();
    		System.out.println("Sorted in: "+(stop-start));
    		
    		request.setAttribute("people", data);
    		getServletContext().getRequestDispatcher("/last.jsp").forward(request, response);
		} catch (NamingException e) {
			System.out.println(e);
		}
	}
	
	private void BrowseByFirstName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			long start = System.currentTimeMillis();
			ArrayList<Person> people = (ArrayList<Person>)new Ldap().connect().getPeople();
			long stop = System.currentTimeMillis();
			System.out.print("Retrieval in: "+(stop-start));
			
			start = System.currentTimeMillis();
    		HashMap<String,ArrayList<Person>> data = new HashMap<String,ArrayList<Person>>();
    		for(Person p : people) {
    			InsertPersonByString(p.getFirstName(),p,data);
    		}
    		stop = System.currentTimeMillis();
    		System.out.println("Bucketed in: "+(stop-start));
    		
    		start = System.currentTimeMillis();
    		for(ArrayList<Person> peeps : data.values()) {
    			Collections.sort(peeps, new Person.ByFirstName());
    		}
    		stop = System.currentTimeMillis();
    		System.out.println("Sorted in: "+(stop-start));
    		
    		request.setAttribute("people", data);
    		getServletContext().getRequestDispatcher("/first.jsp").forward(request, response);
		} catch (NamingException e) {
			System.out.println(e);
		}
	}
	
	private void InsertPersonByString(String name, Person p, HashMap<String,ArrayList<Person>> data) {
		if(name!=null && name.length()!=0) {
			name = name.toUpperCase();
			ArrayList<Person> plist = data.get(String.valueOf(name.charAt(0)));
			if (plist == null) {
				plist = new ArrayList<Person>();
				data.put((String.valueOf(name.charAt(0))), plist);
			}
			plist.add(p);
		} else {
			//Person has no name?!?!
		}
	}
}