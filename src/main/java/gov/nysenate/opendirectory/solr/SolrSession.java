package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import gov.nysenate.opendirectory.models.Person;

public class SolrSession {

	Solr solr;
	SecureLoader loader;
	SecureWriter writer;
	
	@SuppressWarnings("serial")
	public class SolrSessionException extends Exception {
		public SolrSessionException(String m) { super(); }
		public SolrSessionException(String m ,Throwable t) { super(m,t); }
	}
	
	public static void main(String[] args) {
		
		SolrSession solr = new SolrSession(Person.getAnon(),new Solr().connect());
		
		for(int i=0; i<10; i++)
			solr.loadPeople();
	}
	
	public SolrSession(Person user, Solr solr) {
		this.solr = solr;
		this.loader = new SecureLoader(user,this);
		this.writer = new SecureWriter(user,this);
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
		} else { return null; } //TODO: this should throw an exception! 
		//throw new SolrSessionException("UID provided ("+uid+") was not unique in solr!");
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
	
	public void deleteAll(){
		try {
			solr.deleteAll();
			solr.server.commit();
		} catch (SolrServerException e) {
			//TODO: this should throw a SolrSessionException
		} catch (IOException e) {
			//TODO: this should throw a SolrSessionException
		}
	}
	
	
	private void addPerson(Person person) throws SolrServerException, IOException {
		solr.server.add(writer.writePerson(person));
	}
}
