package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.HashMap;

import org.apache.solr.common.SolrDocument;
import javax.xml.parsers.*;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;

import gov.nysenate.opendirectory.models.Person;

public class SecureLoader {

	private DocumentBuilder builder;
	private InputSource source;
	private Person user;
	
	public static void main(String[] args) {
		
		SecureLoader loader = new SecureLoader(Person.getAnon());
		
		String sethash = "uid:public:phone:public, senate:permissions:admin:phone2:senate";
		String stringset = "javascript, python, soccer";
		String stringhash = "bush2:Annabel Bush:williams:Jared Williams:hoppin:Andrew Hoppin";
		
		//Test the set hash loader
		HashMap<String, TreeSet<String>> perms = loader.loadSetHash(sethash);
		for(String key : perms.keySet()) {
			System.out.println(key+": "+perms.get(key).toString());
		}
		
		//Test the stringset loader
		System.out.println(loader.loadStringSet(stringset).toString());
		
		//test the stringhash loader
		HashMap<String, String> bookmarks = loader.loadStringHash(stringhash);
		for(String key : bookmarks.keySet()) {
			System.out.println(key+": "+bookmarks.get(key));
		}
		
		/*
		ArrayList<Float> xml_times = new ArrayList<Float>();
		ArrayList<Float> custom_times = new ArrayList<Float>();
		ArrayList<Float> other_custom_times = new ArrayList<Float>();
		String xml_input = "<fields><field name=\"skills\" allow = \"public\"/><field name=\"uid\" allow = \"public\"/><field name=\"lastName\" allow = \"public\"/><field name=\"phone\" allow = \"public\"/><field name=\"interests\" allow = \"public\"/><field name=\"location\" allow = \"public\"/><field name=\"linkedin\" allow = \"public\"/><field name=\"department\" allow = \"public\"/><field name=\"state\" allow = \"public\"/><field name=\"phone2\" allow = \"public\"/><field name=\"email2\" allow = \"public\"/><field name=\"picture\" allow = \"public\"/><field name=\"user_credential\" allow = \"admin\"/><field name=\"twitter\" allow = \"public\"/><field name=\"bookmarks\" allow = \"admin\"/><field name=\"title\" allow = \"public\"/><field name=\"bio\" allow = \"public\"/><field name=\"email\" allow = \"public\"/><field name=\"irc\" allow = \"public\"/><field name=\"facebook\" allow = \"public\"/><field name=\"permissions\" allow = \"admin\"/><field name=\"fullName\" allow = \"public\"/><field name=\"firstName\" allow = \"public\"/></fields>";
		String input = "skills:public:uid:public:lastName:public:phone:public:interests:public:location:public:linkedin:public:department:public:state:public:phone2:public:email2:public:picture:public:user_credential:admin:twitter:public:bookmarks:public:title:public:bio:public:email:public:irc:public:facebook:public:permissions:admin:fullName:public:firstName:public";
		String other_input = "skills:public\nuid:public\nlastName:public\nphone:public\ninterests:public\nlocation:public\nlinkedin:public\ndepartment:public\nstate:public\nphone2:public\nemail2:public\npicture:public\nuser_credential:admin\ntwitter:public\nbookmarks:public\ntitle:public\nbio:public\nemail:public\nirc:public\nfacebook:public\npermissions:admin\nfullName:public\nfirstName:public";
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource source = new InputSource();
			
			for(int k=0; k<10; k++) {
				try {
					source.setCharacterStream(new StringReader(xml_input));
					builder.parse(source);
					for(int i=0; i<2000; i++) {
						long start = System.nanoTime();
						source = new InputSource();
						source.setCharacterStream(new StringReader(xml_input));
						builder.parse(source);
						long end = System.nanoTime();
						xml_times.add((end-start)/1000f);
					}
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				for(int i=0; i<2000; i++) {
					
					//Faster
					long start = System.nanoTime();
					for(String line : other_input.split("\n")) {
						String[] halves = line.split(":");
						for(String item : halves[1].split(";"))
							item+=halves[0];
					}
					other_custom_times.add((System.nanoTime()-start)/1000f);
					
					//Fastest
					start = System.nanoTime();
					String[] parts = input.split(":");
					for(int j=0; j<parts.length-1; j+=2) {
						for(String item : parts[j+1].split(";"))
							item += parts[j];
					}
					custom_times.add((System.nanoTime()-start)/1000f);
				}
					
				float total = 0;
				for(float time : custom_times)
					total+=time;
				System.out.println("Average custom time: "+total/custom_times.size());
				
				total = 0;
				for(float time : other_custom_times)
					total+=time;
				System.out.println("Average other custom time: "+total/other_custom_times.size());
				
				total = 0;
				for(float time : xml_times)
					total+=time;
				System.out.println("Average xml time: "+total/xml_times.size());
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Done");
		*/
	}
	
	public SecureLoader(Person user) {
		this.user = user;
		
		try {
			//Per-instance objects, might want to make these class static at some point, probably a minor issue? 
			this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.source = new InputSource();
			
			//Warm the builder up, I think this helps fight the lazy loading
			source.setCharacterStream(new StringReader("<hello></hello>"));
			this.builder.parse(source);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Person loadPerson(SolrDocument profile) {
		//Do the loading here
		Person person = new Person();
		
		long start = System.nanoTime();
		String permissions = (String)profile.getFieldValue("permissions");
		String[] parts = permissions.split(":");
		for(int j=0; j<parts.length-1; j+=2) {
			
			if(!isApproved(user,parts[j+1]))
				continue;
			
			String fieldname = parts[j];
			Object fieldvalue = profile.getFieldValue(fieldname);
			
			setFieldValue(person,permissions,fieldname,fieldvalue);
		}
		long end = System.nanoTime();
		//System.out.println((end-start)/1000f+" microsecond load");
		
		return person;
	}

	private boolean isApproved(Person user,String permissions) {
		
		//Auto-grant access to the admin user
		if(user.equals(Person.getAdmin()))
			return true;
		
        //This is the TreeSet that will house credentials for each field
        TreeSet<String> user_credentials = user.getCredentials();
        
        //Break the permissions up and check for matches
		for(String temp : permissions.split(", "))
			if(user_credentials.contains(temp) == true)
    			return true;
		
		return false;
	}
	
	public HashMap<String,String> loadStringHash(String str) {
		HashMap<String,String> map = new HashMap<String,String>();
		if(str!=null) {
			String[] parts = str.split(":");
			for(int i=0; i<parts.length-1; i+=2) {
				map.put(parts[i],parts[i+1]);
			}
		}
		return map;
	}
	
	public HashMap<String,TreeSet<String>> loadSetHash(String str) {
		HashMap<String,TreeSet<String>> map = new HashMap<String,TreeSet<String>>();
		if(str!=null) {
			String[] parts = str.split(":");
			for(int i=0; i<parts.length-1; i+=2)
				map.put(parts[i], loadStringSet(parts[i+1]));
		}
		return map;
	}
	
	public TreeSet<String> loadStringSet(String str) {
		if(str==null || str.isEmpty())
			return new TreeSet<String>();
		return new TreeSet<String>(Arrays.asList(str.split(", ")));
	}
	
	private void setFieldValue(Person person, String permissions, String fieldname, Object fieldvalue) {
		//Switch in the fieldname for speed
		if(fieldname.equals("bio"))
			person.setBio((String)fieldvalue);
		else if(fieldname.equals("bookmarks"))
			person.setBookmarks(loadStringHash((String)fieldvalue));
		else if(fieldname.equals("department"))
			person.setDepartment((String)fieldvalue);
		else if(fieldname.equals("email"))
			person.setEmail((String)fieldvalue);
		else if(fieldname.equals("email2"))
			person.setEmail2((String)fieldvalue);
		else if(fieldname.equals("facebook"))
			person.setFacebook((String)fieldvalue);
		else if(fieldname.equals("firstName"))
			person.setFirstName((String)fieldvalue);
		else if(fieldname.equals("fullName"))
			person.setFullName((String)fieldvalue);
		else if(fieldname.equals("interests"))
			person.setInterests(loadStringSet((String)fieldvalue));
		else if(fieldname.equals("irc"))
			person.setIrc((String)fieldvalue);
		else if(fieldname.equals("lastName"))
			person.setLastName((String)fieldvalue);
		else if(fieldname.equals("linkedin"))
			person.setLinkedin((String)fieldvalue);
		else if(fieldname.equals("location"))
			person.setLocation((String)fieldvalue);
		else if(fieldname.equals("permissions"))
			person.setPermissions(loadSetHash(permissions));
		else if(fieldname.equals("phone"))
			person.setPhone((String)fieldvalue);
		else if(fieldname.equals("phone2"))
			person.setPhone2((String)fieldvalue);
		else if(fieldname.equals("picture"))
			person.setPicture((String)fieldvalue);
		else if(fieldname.equals("skills"))
			person.setSkills(loadStringSet((String)fieldvalue));
		else if(fieldname.equals("state"))
			person.setState((String)fieldvalue);
		else if(fieldname.equals("title"))
			person.setTitle((String)fieldvalue);
		else if(fieldname.equals("twitter"))
			person.setTwitter((String)fieldvalue);
		else if(fieldname.equals("uid"))
			person.setUid((String)fieldvalue);
		else if(fieldname.equals("user_credential"))
			person.setCredentials(loadStringSet((String)fieldvalue));
	}
	
	/*
	private void setField(Person person, NodeList fields, String fieldname, Object fieldvalue) {
		//Switch in the fieldname for speed
		if(fieldname.equals("bio"))
			person.setBio((String)fieldvalue);
		else if(fieldname.equals("bookmarks"))
			person.setBookmarks(loadBookmarks((String)fieldvalue));
		else if(fieldname.equals("department"))
			person.setDepartment((String)fieldvalue);
		else if(fieldname.equals("email"))
			person.setEmail((String)fieldvalue);
		else if(fieldname.equals("email2"))
			person.setEmail2((String)fieldvalue);
		else if(fieldname.equals("facebook"))
			person.setFacebook((String)fieldvalue);
		else if(fieldname.equals("firstName"))
			person.setFirstName((String)fieldvalue);
		else if(fieldname.equals("fullName"))
			person.setFullName((String)fieldvalue);
		else if(fieldname.equals("interests"))
			person.setInterests(parseTreeSet((String)fieldvalue));
		else if(fieldname.equals("irc"))
			person.setIrc((String)fieldvalue);
		else if(fieldname.equals("lastName"))
			person.setLastName((String)fieldvalue);
		else if(fieldname.equals("linkedin"))
			person.setLinkedin((String)fieldvalue);
		else if(fieldname.equals("location"))
			person.setLocation((String)fieldvalue);
		else if(fieldname.equals("permissions"))
			person.setPermissions(buildHashMap(fields));
		else if(fieldname.equals("phone"))
			person.setPhone((String)fieldvalue);
		else if(fieldname.equals("phone2"))
			person.setPhone2((String)fieldvalue);
		else if(fieldname.equals("picture"))
			person.setPicture((String)fieldvalue);
		else if(fieldname.equals("skills"))
			person.setSkills(parseTreeSet((String)fieldvalue));
		else if(fieldname.equals("state"))
			person.setState((String)fieldvalue);
		else if(fieldname.equals("title"))
			person.setTitle((String)fieldvalue);
		else if(fieldname.equals("twitter"))
			person.setTwitter((String)fieldvalue);
		else if(fieldname.equals("uid"))
			person.setUid((String)fieldvalue);
		else if(fieldname.equals("user_credential"))
			person.setCredentials(parseTreeSet((String)fieldvalue));
	}
	
	public TreeSet<String> parseTreeSet(String input){
		TreeSet<String> set = new TreeSet<String>();
		for(String temp : input.split(","))
			set.add(temp);
		return set;
	}
	
	public HashMap<String,TreeSet<String>> buildHashMap(NodeList fields) {
		HashMap<String, TreeSet<String>> map = new HashMap<String, TreeSet<String>>();
		for(int i=0; i<fields.getLength(); i++) {
			map.put(
					(String)fields.item(i).getAttributes().item(1).getNodeValue(),
					parseTreeSet((String)fields.item(i).getAttributes().item(0).getNodeValue())
				);
		}
		return map;
	}
	
	public HashMap<String, TreeSet<String>> loadBookmarks(String bookmarks_xml){
		HashMap<String, TreeSet<String>> bookmarks = new HashMap<String, TreeSet<String>>();
	    
		try {
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(bookmarks_xml));
	        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	        bookmarks = buildHashMap(doc.getDocumentElement().getChildNodes());
		} catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return bookmarks;
	}
	*/
}
