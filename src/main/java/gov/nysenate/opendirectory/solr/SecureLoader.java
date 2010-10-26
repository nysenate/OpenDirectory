package gov.nysenate.opendirectory.solr;

import org.apache.solr.common.SolrDocument;

import gov.nysenate.opendirectory.models.Person;

public class SecureLoader {

	private Person user;
	
	public SecureLoader(Person user) {
		this.user = user;
	}
	
	public Person loadPerson(SolrDocument profile) {
		//Do the loading here
		return null;
	}
	
}
