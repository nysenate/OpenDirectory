package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;


import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BrowseServlet extends BaseServlet {
	// TODO This bucketting could probably be done in a much better way
	
	public class BrowseServletException extends Exception {
		public BrowseServletException(String m) { super(m); }
		public BrowseServletException(String m, Throwable t) { super(m,t); }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		try {
			try {
				String command = urls.getCommand(request);
				System.out.println(command);
			    if (command != null) {
			    	if ( command.equals("department")) {
			    		long start = System.nanoTime();
			    		
			    		request.setAttribute( "people",
			    				getDepartmentBuckets(self,
			    							Person.class.getMethod("getDepartment"),
			    							new Person.ByDepartment(),
			    							response.getWriter()));
			    		
						System.out.println("Sort by Department: "+(System.nanoTime()-start)/1000000f+" milliseconds");
						self.render("dept.jsp");
						
			    	} else if ( command.equals("firstname") ) {
			    		long start = System.nanoTime();
						request.setAttribute( "people",
								GetPeopleSortedByChar(self,
										Person.class.getMethod("getFirstName"),
										new Person.ByFirstName()
									));
						System.out.println("Sort by Firstname: "+(System.nanoTime()-start)/1000000f+" milliseconds");
						self.render("first.jsp");
						
			    	} else if ( command.equals("lastname") ) {
			    		long start = System.nanoTime();
						request.setAttribute( "people",
								GetPeopleSortedByChar(self,
										Person.class.getMethod("getLastName"),
										new Person.ByLastName()
									));
						System.out.println("Sort by LastName: "+(System.nanoTime()-start)/1000000f+" milliseconds");
			    		self.render("last.jsp");
			    		
			    	} else if ( command.equals("location") ) {
			    		long start = System.nanoTime();
						request.setAttribute( "people",
								GetPeopleSortedByString(self,
										Person.class.getMethod("getLocation"),
										new Person.ByLocation()
									));
						System.out.println("Sort by Location: "+(System.nanoTime()-start)/1000000f+" milliseconds");
						self.render("loc.jsp");
						
			    	}
			    	
			    } else {
			    	self.render("index.jsp");
			    }
			} catch (NoSuchMethodException e) {
				throw new BrowseServletException("Somehow the Java bean methods don't exist.");
			} catch (SecurityException e) {
				throw new BrowseServletException("Somehow the BrowseServlet does not have permissions to access the getMethods.");
			}
			
		} catch (BrowseServletException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			if(e.getCause()!=null)
				e.getCause().printStackTrace();
			// TODO something reasonable to resolve things here
		}
		
	}
		
	private HashMap<String,TreeSet<Person>> GetPeopleSortedByChar(Request self, Method method, Comparator<Person> comparator) throws BrowseServletException {
		try {
			long start = System.nanoTime();
			ArrayList<Person> people = self.solrSession.loadPeople();
			System.out.println("loading people in: "+(System.nanoTime()-start)/1000000f+" milliseconds");
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
						plist.add(p);
					}
				} catch (NullPointerException e) {
					//Person doesn't have a `value`
					e.printStackTrace();
				}
			}
			return data;
		} catch (InvocationTargetException e) {
			//Person is not a valid target for the method
			System.out.println(e);
		} catch (IllegalAccessException e) {
			//Method is private and not accessible
			System.out.println(e);
		}
		return null;
	}
	
	private HashMap<String,HashMap<String,TreeSet<Person>>> getDepartmentBuckets(Request self, Method method, Comparator<Person> comparator, PrintWriter out) throws BrowseServletException {
		HashMap<String, TreeSet<Person>> treePeople = GetPeopleSortedByString(self, method, comparator);
		HashMap<String,HashMap<String,TreeSet<Person>>> data = new HashMap<String,HashMap<String,TreeSet<Person>>>();
		
		List<String> names = Arrays.asList("Creative Services","Legislative Committee","Maintenance and Operations","Senate Services","Senate Technology Services","Task Force","Student Programs","Select Committee","Senators","Caucuses","Senate Finance Committee");
		
		for(String department:treePeople.keySet()) {
			String[] tuple = department.split("/");
			HashMap<String,TreeSet<Person>> tMap = null;
			if(tuple.length > 1 && names.contains(tuple[0])) {
				//department = LDAP name e.g. "M&O/Post Office
				//tuple[0] = organization e.g. 'M&O' tuple[1] = dept e.g. 'Post Office'
				tMap = bucketHelper(data,tuple[0]);
				
				tMap.put(tuple[1],treePeople.get(department));
				department = tuple[0];
			}
			else {
				if(department.startsWith("Majority") || department.startsWith("Maj.")) {
					tMap = bucketHelper(data,"Majority");
					tMap.put(department, treePeople.get(department));
					department = "Majority";
				}
				else if(department.startsWith("Minority") || department.startsWith("Min.")) {
					tMap = bucketHelper(data,"Minority");
					tMap.put(department, treePeople.get(department));
					department = "Minority";
				}
				else if(department.startsWith("Senator")) {
					tMap = bucketHelper(data,"Senator");
					tMap.put(department, treePeople.get(department));
					department = "Senator";
				}
				else {
					tMap = new HashMap<String,TreeSet<Person>>();
					tMap.put(department, treePeople.get(department));

				}
			}
			data.put(department,tMap);
		}
		
		return data;
	}
	
	public HashMap<String,TreeSet<Person>> bucketHelper(HashMap<String,HashMap<String,TreeSet<Person>>> data, String key) {
		HashMap<String,TreeSet<Person>> tMap = null;
		if((tMap = data.get(key)) != null) {
			return tMap;
		}
		return new HashMap<String,TreeSet<Person>>();
	}

	private HashMap<String,TreeSet<Person>> GetPeopleSortedByString(Request self,Method method, Comparator<Person> comparator) throws BrowseServletException {
		try {
			long start = System.nanoTime();
			ArrayList<Person> people = self.solrSession.loadPeople();
			System.out.println((System.nanoTime()-start)/1000000f+" ms - Load People");
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
						plist.add(p);
					}
				} catch (NullPointerException e) {
					//Person doesn't have a `value`
					e.printStackTrace();
				}
			}
			return data;
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