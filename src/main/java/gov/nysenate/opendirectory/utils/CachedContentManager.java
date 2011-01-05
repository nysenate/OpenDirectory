package gov.nysenate.opendirectory.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.BrowseServlet.BrowseServletException;

public class CachedContentManager {
	
	public static enum BrowseType {
		FIRST_NAME,
		LAST_NAME,
		DEPARTMENT,
		LOCATION
	}
	
	public static String getCacheKey(BrowseType browseType, Request self) throws SecurityException, BrowseServletException, NoSuchMethodException, IOException {
		String cacheKey = "";
		
		if(self.user == null || self.user.getUid() == null || self.user.getUid().equals("")) {
			cacheKey += "ANON_";
		}
		else {
			cacheKey += "SENATE_";
		}
		
		long start = System.nanoTime();
		switch(browseType) {
			case FIRST_NAME: 
				cacheKey += "FIRST_NAME";
				self.httpRequest.setAttribute( "people",
						GetPeopleSortedByChar(self,
								Person.class.getMethod("getFirstName"),
								new Person.ByFirstName()
							));
				break;
			case LAST_NAME: 
				cacheKey += "LAST_NAME";
				self.httpRequest.setAttribute( "people",
						GetPeopleSortedByChar(self,
								Person.class.getMethod("getLastName"),
								new Person.ByLastName()
							));
				break;
			case DEPARTMENT: 
				cacheKey += "DEPARTMENT";
				self.httpRequest.setAttribute( "people",
	    				getDepartmentBuckets(self,
	    							Person.class.getMethod("getDepartment"),
	    							new Person.ByDepartment()));
				break;
			case LOCATION: 
				cacheKey += "LOCATION";
				self.httpRequest.setAttribute( "people",
						GetPeopleSortedByString(self,
								Person.class.getMethod("getLocation"),
								new Person.ByLocation()
							));
				break;
		}
		System.out.println("Caching: " + cacheKey + ", loaded in "+(System.nanoTime()-start)/1000000f+" milliseconds");
		
		return cacheKey;
	}
	
	private static HashMap<String,TreeSet<Person>> GetPeopleSortedByChar(Request self, Method method, Comparator<Person> comparator) throws BrowseServletException {
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
	
	private static HashMap<String,HashMap<String,TreeSet<Person>>> getDepartmentBuckets(Request self, Method method, Comparator<Person> comparator) throws BrowseServletException {
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
	
	public static HashMap<String,TreeSet<Person>> bucketHelper(HashMap<String,HashMap<String,TreeSet<Person>>> data, String key) {
		HashMap<String,TreeSet<Person>> tMap = null;
		if((tMap = data.get(key)) != null) {
			return tMap;
		}
		return new HashMap<String,TreeSet<Person>>();
	}

	private static HashMap<String,TreeSet<Person>> GetPeopleSortedByString(Request self,Method method, Comparator<Person> comparator) throws BrowseServletException {
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
