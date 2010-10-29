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
		Person person = new Person();
		person.setFirstName((String)profile.getFieldValue("firstName"));
		person.setLastName((String)profile.getFieldValue("lastName"));
		person.setFullName((String)profile.getFieldValue("fullName"));
		person.setDepartment((String)profile.getFieldValue("department"));
		person.setLocation((String)profile.getFieldValue("location"));
		person.setEmail((String)profile.getFieldValue("email"));
		person.setPhone((String)profile.getFieldValue("phone"));
		person.setTitle((String)profile.getFieldValue("title"));
		person.setState((String)profile.getFieldValue("state"));
		person.setUid((String)profile.getFieldValue("id"));
		return person;
	}
	
}
