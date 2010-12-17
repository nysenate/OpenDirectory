package gov.nysenate.opendirectory.utils;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.SolrSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

public class SerialUtils {
	
	public static void main(String[] args) {
		
		String sethash = "uid:public:phone:public senate:permissions:admin:phone2:senate";
		String stringset = "javascript python soccer";
		String stringhash = "bush2:Annabel Bush:williams:Jared Williams:hoppin:Andrew Hoppin";
		
		//Test the set hash loader
		HashMap<String, TreeSet<String>> perms = loadSetHash(sethash);
		for(String key : perms.keySet()) {
			System.out.println(key+": "+perms.get(key).toString());
		}
		
		//Test the string set loader
		System.out.println(loadStringSet(stringset).toString());
		
		//test the string hash loader
		HashMap<String, String> bookmarks = loadStringHash(stringhash);
		for(String key : bookmarks.keySet()) {
			System.out.println(key+": "+bookmarks.get(key));
		}
	}
	
	public static HashMap<String,String> loadStringHash(String str) {
		HashMap<String,String> map = new HashMap<String,String>();
		if(str!=null && !str.isEmpty()) {
			String[] parts = str.split(":");
			for(int i=0; i<parts.length-1; i+=2) {
				map.put(parts[i],parts[i+1]);
			}
		}
		return map;
	}
	
	public static HashMap<String,TreeSet<String>> loadSetHash(String str) {
		HashMap<String,TreeSet<String>> map = new HashMap<String,TreeSet<String>>();
		if(str!=null && !str.isEmpty()) {
			String[] parts = str.split(":");
			for(int i=0; i<parts.length-1; i+=2)
				map.put(parts[i], loadStringSet(parts[i+1]));
		}
		return map;
	}
	
	public static TreeSet<String> loadStringSet(String str) {
		if(str==null || str.isEmpty())
			return new TreeSet<String>();
		return new TreeSet<String>(Arrays.asList(str.split(" ")));
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

	public static String writeStringHash(HashMap<String, String> map) {
		if(map==null)
			return "";
		
		String str = "";
		for(String key : map.keySet()) {
			if(!str.isEmpty())
				str+=":";
			str+=key+":"+map.get(key);
		}
		return str;
	}
	
	public static String writeSetHash(HashMap<String, TreeSet<String>> map) {
		if(map==null)
			return "";
		
		String str = "";
		for(String key : map.keySet()) {
			if(!str.isEmpty())
				str+=":";
			str+=key+":"+writeStringSet(map.get(key));
		}
		return str;
	}
	
	public static String writeStringSet(TreeSet<String> set) {
		if(set == null)
			return "";
		
		String str = "";
		for(String s : set)
			str += s+" ";
		
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
