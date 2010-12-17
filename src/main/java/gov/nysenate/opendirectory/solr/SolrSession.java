package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.SerialUtils;

public class SolrSession {

	Solr solr;
	SecureLoader loader;
	
	@SuppressWarnings("serial")
	public class SolrSessionException extends Exception {
		public SolrSessionException(String m) { super(); }
		public SolrSessionException(String m ,Throwable t) { super(m,t); }
	}
	
	public static void main(String[] args) {
		
		SolrSession solr = new SolrSession(Person.getAnon(),new Solr().connect());
		
		for(int i=0; i<10; i++)
			solr.loadPeople();
		
		/*
		//Test bookmark writing
		HashMap<String,String> bookmarks = new HashMap<String,String>();
		bookmarks.put("williams", "Jared Williams");
		bookmarks.put("hoppin","Andrew Hoppin");
		bookmarks.put("bush2","Annabel Bush");
		System.out.println(solr.writeStringHash(bookmarks));
		
		//Test permissions writing
		HashMap<String,TreeSet<String>> perms = new HashMap<String,TreeSet<String>>();
		perms.put("uid", new TreeSet<String>(Arrays.asList("public")));
		perms.put("phone", new TreeSet<String>(Arrays.asList("senate","public")));
		perms.put("phone2", new TreeSet<String>(Arrays.asList("senate")));
		perms.put("permissions", new TreeSet<String>(Arrays.asList("admin")));
		System.out.println(solr.writeSetHash(perms));
		
		//Test Interest/Skills writing
		TreeSet<String> skills = new TreeSet<String>(Arrays.asList("python","soccer","javascript"));
		System.out.println(solr.writeStringSet(skills));
		*/
		
		System.out.println("Done");
	}
	
	public SolrSession(Person user, Solr solr) {
		this.solr = solr;
		this.loader = new SecureLoader(user,this);
	}
	
	public Person loadPersonByUid(String uid) {
		
		//Do the query on the uid field
		QueryResponse results = solr.query("uid:"+uid);
		SolrDocumentList profiles = results.getResults();
		
		//Return null on no results, sometimes getResults returns null
		if( profiles==null || profiles.getNumFound() == 0 ) {
			return null;
			
		//Load a person from the profile if 1 result
		} else if ( profiles.getNumFound() == 1 ) {
			return loader.loadPerson(profiles.get(0));
			
		//This should never happen since uid is unique in the SOLR config file.
		} else
			return null;
	}
	
	public ArrayList<Person> loadPeopleByQuery(String query) {
		
		System.out.println("\nLoading People By Query: "+query);
		System.out.println("===============================================");
		
		//Execute the query
		long start = System.nanoTime();
		QueryResponse results = solr.query(query,2000);
		
		if(results==null)
			return new ArrayList<Person>();
		
		SolrDocumentList profiles = results.getResults();
		System.out.println((System.nanoTime()-start)/1000000f+" ms - query to solr");
		
		if(profiles==null)
			return new ArrayList<Person>();
		
		//Transform the results
		start = System.nanoTime();
		ArrayList<Person> people = new ArrayList<Person>();
		for( SolrDocument profile : profiles )
			people.add(loader.loadPerson(profile));
		
		System.out.println((System.nanoTime()-start)/1000000f+" ms - load Person array");
		
		return people;
	}
	
	public ArrayList<Person> loadPeople() {
		//Use the otype field to locate all person documents
		return loadPeopleByQuery("otype:person");
	}
	
	//Could use addBean function that comes with solrj but we need to 
	//put the credentials (hashmap) into solr field.
	private void addPerson(Person person) throws SolrServerException, IOException {
		SolrInputDocument solr_person = new SolrInputDocument();	 
		
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
		
		solr_person.addField("permissions", SerialUtils.writeSetHash(person.getPermissions()));
		solr_person.addField("user_credential", SerialUtils.writeStringSet(person.getCredentials()));
		solr_person.addField("bookmarks", SerialUtils.writeBookmarks(person.getBookmarks()));
		
		//additional contact info
		solr_person.addField("bio", person.getBio(), 1.0f);
		solr_person.addField("picture", person.getPicture(), 1.0f);
		solr_person.addField("email2", person.getEmail2(), 1.0f);
		solr_person.addField("phone2", person.getPhone2(), 1.0f);
		solr_person.addField("twitter", person.getTwitter(), 1.0f);
		solr_person.addField("facebook", person.getFacebook(), 1.0f);
		solr_person.addField("linkedin", person.getLinkedin(), 1.0f);
		solr_person.addField("irc", person.getIrc(), 1.0f);
		solr_person.addField("skills", SerialUtils.writeStringSet(person.getSkills()), 1.0f);
		solr_person.addField("interests", SerialUtils.writeStringSet(person.getInterests()), 1.0f);
		
		solr.server.add(solr_person);
	}
	
	public void savePerson(Person person) throws SolrServerException, IOException {
		addPerson(person);
		solr.server.commit();
	}
	
	public void savePeople(Collection<Person> people)  throws SolrServerException, IOException  {
		for(Person person : people)
			addPerson(person);
		solr.server.commit();
	}
	
	public void optimize() throws SolrServerException, IOException {
		solr.server.optimize();
	}
	
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
	
	public void deleteByUid(String uid) {
		try {
			solr.delete("uid:"+uid);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
