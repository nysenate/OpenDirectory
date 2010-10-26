package gov.nysenate.opendirectory.solr;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import gov.nysenate.opendirectory.models.Person;

public class SolrSession {

	private SecureLoader loader;
	private Solr solr;
	
	public SolrSession(Person user, Solr solr) {
		this.loader = new SecureLoader(user);
		this.solr = solr;
	}
	
	public Person loadPersonByName(String name) {
		
		//Do the query
		QueryResponse results = solr.query("fullname:"+name);
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
	
	public void savePerson(Person person) {
		
	}
}
