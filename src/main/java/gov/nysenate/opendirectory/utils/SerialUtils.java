package gov.nysenate.opendirectory.utils;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.SolrSession;

import java.util.Arrays;
import java.util.TreeSet;

public class SerialUtils {
	
	public static void main(String[] args) {
		
		String stringset = "javascript, python, soccer";
		
		//Test the string set loader
		System.out.println(loadStringSet(stringset).toString());
		
	}
	
	public static TreeSet<String> loadStringSet(String str) {
		if(str==null || str.isEmpty())
			return new TreeSet<String>();
		return new TreeSet<String>(Arrays.asList(str.split(", ")));
	}
	
	public static TreeSet<Person> loadBookmarks(String str,Person person, SolrSession session) {
		
		//Handle the empty case
		if(str==null || str.isEmpty())
			return new TreeSet<Person>();
		
		String query = "";
		for(String uid : str.split(" ")) {
			
			//Avoid recursion, shouldn't happen anyway..
			if(uid.equals(person.getUid()))
				continue;
			
			query += ((query.isEmpty()) ? "" : " OR ")+"uid:"+uid;
		}
		
		return new TreeSet<Person>(session.loadPeopleByQuery(query));
	}
	
	public static String writeStringSet(TreeSet<String> set) {
		if(set == null)
			return "";
		
		String str = "";
		for(String s : set)
			str += s+", ";
		
		return str;
	}
	
	public static String writeBookmarks(TreeSet<Person> marks) {
		if(marks == null)
			return "";
		
		String str = "";
		for(Person mark : marks)
			str += mark.getUid()+" ";
		return str;
	}
}
