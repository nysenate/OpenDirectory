package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
//import java.lang.reflect.Field;

import gov.nysenate.opendirectory.models.Person;

public class SolrSession {

	private SecureLoader loader;
	private Solr solr;
	
	public SolrSession(Person user, Solr solr) {
		this.loader = new SecureLoader(user);
		this.solr = solr;
	}
	
	public Person loadPersonByUid(String uid) {
		//Do the query
		QueryResponse results = solr.query("uid:"+uid);
		SolrDocumentList profiles = results.getResults();
		
		//Return null on no results
		if( profiles.getNumFound() == 0 ) {
			return null;
			
		//Load a person from the profile if 1 result
		} else if ( profiles.getNumFound() == 1 ) {
			return loader.loadPerson(profiles.get(0));
			
		//Throw some sort of exception on multiple matches
		} else {
			//Too many people
			//Throw some kind of error
			return null;
		}
	}
	
	public ArrayList<Person> loadPeopleByQuery(String query) {
		//Do the query
		QueryResponse results = solr.query(query);
		SolrDocumentList profiles = results.getResults();
		
		//Transform the results
		ArrayList<Person> people = new ArrayList<Person>();
		for( SolrDocument profile : profiles ) {
			people.add(loader.loadPerson(profile));
		}
		return people;
	}
	
	public Person loadPersonByName(String name) {
		
		//Do the query
		QueryResponse results = solr.query("fullName:"+name);
		SolrDocumentList profiles = results.getResults();
		
		//Return null on no results
		if( profiles.getNumFound() == 0 ) {
			return null;
			
		//Load a person from the profile if 1 result
		} else if ( profiles.getNumFound() == 1 ) {
			return loader.loadPerson(profiles.get(0));
			
		//Throw some sort of exception on multiple matches
		} else {
			//Too many people
			//Throw some kind of error
			return null;
		}
	}
	
	public ArrayList<Person> loadPeople() {
		return loadPeopleByQuery("*:*");
	}
	
	//Could use addBean function that comes with solrj but we need to 
	//put the credentials (hashmap) into solr field.
	private void addPerson(Person person) throws SolrServerException, IOException {
		SolrInputDocument solr_person = new SolrInputDocument();
		
		//If this person is just being pulled in from LDAP and is going to be added into Solr
		if(person.getPermissions()==null)
		{
			//Initialize the rest of the person variables
			TreeSet<String> cred_default = new TreeSet<String>();
			cred_default.add("public");
			
			person.setPermissions(Person.getDefaultPermissions());
			person.setCredentials(cred_default);
			
			person.setBio("");
			person.setEmail2("");
			person.setPhone2("");
			person.setTwitter("");
			person.setFacebook("");
			person.setLinkedin("");
			person.setIrc("");
			person.setSkills(null);
			person.setInterests(null);
			person.setBookmarks(null);
			person.setPicture("");
		}	
		
		//to pull in HashMap Permissions 
		String permissions = Permissions(person.getPermissions());
		
		//to pull in HashMap bookmarks
		String bookmarks="";
		if(person.getBookmarks()!=null)
			bookmarks= Bookmarks(person.getBookmarks());
		
		//to pull in interests and skills TreeSets
		String credentials = person.getCredentials().toString().substring(1, person.getCredentials().toString().length()-1);
		String skills="";
		String interests="";
		
		if(person.getSkills()!=null)
			skills = person.getSkills().toString().substring(1, person.getSkills().toString().length()-1);
		
		if(person.getInterests()!=null)
			interests = person.getInterests().toString().substring(1, person.getInterests().toString().length()-1);
			
		solr_person.addField("otype", "person", 1.0f);
		solr_person.addField("firstName", person.getFirstName(), 1.0f);
		solr_person.addField("lastName", person.getLastName(), 1.0f);
		solr_person.addField("title", person.getTitle(), 1.0f);
		solr_person.addField("uid", person.getUid(), 1.0f);
		solr_person.addField("fullName", person.getFullName(), 1.0f);
		solr_person.addField("state", person.getState(), 1.0f);
		solr_person.addField("location", person.getLocation(), 1.0f);
		solr_person.addField("department", person.getDepartment(), 1.0f);
		solr_person.addField("phone", person.getPhone(), 1.0f);
		solr_person.addField("email", person.getEmail(), 1.0f);
		
		solr_person.addField("permissions", permissions);
		solr_person.addField("user_credential", credentials);
		//add bookmarks
		solr_person.addField("bookmarks", bookmarks);
		
		//additional contact info
		solr_person.addField("bio", person.getBio(), 1.0f);
		solr_person.addField("picture", person.getPicture(), 1.0f);
		solr_person.addField("email2", person.getEmail2(), 1.0f);
		solr_person.addField("phone2", person.getPhone2(), 1.0f);
		solr_person.addField("twitter", person.getTwitter(), 1.0f);
		solr_person.addField("facebook", person.getFacebook(), 1.0f);
		solr_person.addField("linkedin", person.getLinkedin(), 1.0f);
		solr_person.addField("irc", person.getIrc(), 1.0f);
		solr_person.addField("skills", skills, 1.0f);
		solr_person.addField("interests", interests,1.0f);
		
		solr.server.add(solr_person);
	}
	
	public void savePerson(Person person) throws SolrServerException, IOException {
		addPerson(person);
		solr.server.commit();
	}
	
	public void savePeople(Collection<Person> people)  throws SolrServerException, IOException  {
		for(Person person : people) {
			addPerson(person);
		}
		solr.server.commit();
	}

/* FOR DYNAMIC FIELDS	
 *	To be figured out... need to figure out annotations with Graylin
 * public AnnotatedField getAnnotatedField(Field field) {
		org.apache.solr.client.solrj.beans.Field lf = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
		if(lf != null) {
			return new AnnotatedField(lf);
		}
		return null;		
	}
*/
	
	public void deleteAll() {
		try {
			solr.deleteAll();
			solr.server.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Returns permissions for each field in "xml" string
	public String Permissions(HashMap<String,TreeSet<String>> permissions)
	{
		Iterator<?> permission = permissions.keySet().iterator();
		
		//XML to be written
		String credentials = new String();
		credentials="<fields>";
		
		String field;
		String credential_list;
		String access_level;
		
		while(permission.hasNext())
		{
			field = permission.next().toString();
			credential_list = permissions.get(field).toString();
			access_level = credential_list.substring(1, credential_list.length()- 1);
			
			credentials+="<field name=\"" + field + "\" allow = \"" + 
				access_level + "\"/>"; 
			
		}
		credentials+="</fields>";
		return credentials;
	}
	
	public String Bookmarks(HashMap<String, TreeSet<String>> BOOKMARK)
	{
		Iterator<?> bookmark_iterator = BOOKMARK.keySet().iterator();
		
		//XML to be written
		String bookmarks= new String();
		bookmarks="<users>";
		
		String id;
		String employee_list;
		String fullname;
		
		while(bookmark_iterator.hasNext())
		{
			id = bookmark_iterator.next().toString();
			employee_list = BOOKMARK.get(id).toString();
			fullname = employee_list.substring(1, employee_list.length()-1);
			
			bookmarks+="<user id=\"" + id + "\" fullName = \"" + fullname + "\"/>";
		}
		
		bookmarks+="</users>";
		return bookmarks;
	}
}
