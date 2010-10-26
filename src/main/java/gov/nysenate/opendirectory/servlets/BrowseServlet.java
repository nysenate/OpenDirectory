package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;
 
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BrowseServlet extends HttpServlet {
	
	public BrowseServlet() {
		//Only executed on startup
		
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
			tokens.nextToken(); //Throw `OpenDirectory` away
		    tokens.nextToken(); //Throw `browse` away
		    if (tokens.hasMoreTokens()) {
		    	String type = tokens.nextToken().toLowerCase();
		    	if ( type.equals("department")) {
					request.setAttribute(
							"people",
							GetPeopleSortedByString(
									Person.class.getMethod("getDepartment"),
									new Person.ByDepartment()
								)
						);
					getServletContext().getRequestDispatcher("/dept.jsp").forward(request, response);
					
		    	} else if ( type.equals("firstname") ) {
					request.setAttribute(
							"people",
							GetPeopleSortedByChar(
									Person.class.getMethod("getFirstName"),
									new Person.ByFirstName()
								)
						);
					getServletContext().getRequestDispatcher("/first.jsp").forward(request, response);
					
		    	} else if ( type.equals("lastname") ) {
					request.setAttribute("people",
							GetPeopleSortedByChar(
								Person.class.getMethod("getLastName"),
								new Person.ByLastName())
						);
		    		getServletContext().getRequestDispatcher("/last.jsp").forward(request, response);
		    		
		    	} else if ( type.equals("location") ) {
					request.setAttribute(
							"people",
							GetPeopleSortedByString(
									Person.class.getMethod("getLocation"),
									new Person.ByLocation()
								)
						);
					getServletContext().getRequestDispatcher("/loc.jsp").forward(request, response);
					
		    	}
		    } else {
		    	getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
		    }
		} catch (NoSuchMethodException e) {
			//If the method by name doesn't exist
			System.out.println(e);
		} catch (SecurityException e) {
			//If the method isn't gettable I think? not sure what `security` means
			System.out.println(e);
		}
	}
		
	private HashMap<String,TreeSet<Person>> GetPeopleSortedByChar(Method method, Comparator<Person> comparator) {
		try {
			long start = System.currentTimeMillis();
			ArrayList<Person> people = (ArrayList<Person>)new Ldap().connect().getPeople();
			System.out.println("LDAP Query Time: "+(System.currentTimeMillis()-start)+" milliseconds");
			HashMap<String,TreeSet<Person>> data = new HashMap<String,TreeSet<Person>>();
			for(Person p : people) {
				try {
					String value = (String)method.invoke(p);
					if (value!=null && value.length()!=0) {
						value = value.toUpperCase();
						TreeSet<Person> plist = data.get(String.valueOf(value.charAt(0)));
						if (plist == null) {
							plist = new TreeSet<Person>(comparator);
							data.put((String.valueOf(value.charAt(0))), plist);
						}
						System.out.println(p.getFullName());
						plist.add(p);
					}
				} catch (NullPointerException e) {
					//Person doesn't have a `value`
					e.printStackTrace();
				}
			}
			return data;
		} catch (NamingException e) {
			//Error Connecting to LDAP
			System.out.println(e);
		} catch (InvocationTargetException e) {
			//Person is not a valid target for the method
			System.out.println(e);
		} catch (IllegalAccessException e) {
			//Method is private and not accessible
			System.out.println(e);
		}
		return null;
	}

	private HashMap<String,TreeSet<Person>> GetPeopleSortedByString(Method method, Comparator<Person> comparator) {
		try {
			long start = System.currentTimeMillis();
			ArrayList<Person> people = (ArrayList<Person>)new Ldap().connect().getPeople();
			System.out.println("LDAP Query Time: "+(System.currentTimeMillis()-start)+" milliseconds");
			HashMap<String,TreeSet<Person>> data = new HashMap<String,TreeSet<Person>>();
			for(Person p : people) {
				try {
					String value = (String)method.invoke(p);
					if (value!=null && value.length()!=0) {
						TreeSet<Person> plist = data.get(value);
						if (plist == null) {
							plist = new TreeSet<Person>(comparator);
							data.put(value, plist);
						}
						System.out.println(p.getFullName());
						plist.add(p);
					}
				} catch (NullPointerException e) {
					//Person doesn't have a `value`
					e.printStackTrace();
				}
			}
			return data;
		} catch (NamingException e) {
			//Error Connecting to LDAP
			System.out.println(e);
		} catch (InvocationTargetException e) {
			//Person is not a valid target for the method
			System.out.println(e);
		} catch (IllegalAccessException e) {
			//Method is private and not accessible
			System.out.println(e);
		}
		return null;
	}
}