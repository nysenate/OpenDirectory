package gov.nysenate.opendirectory.solr;

import java.util.TreeSet;

import org.apache.solr.common.SolrDocument;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.SerialUtils;

public class SecureLoader {

	private Person user;
	private SolrSession session;
	
	public static void main(String[] args) {
		
		SolrSession session = new Solr().connect().newSession(Person.getAdmin());
		session.loadPersonByUid("opendirectory");
		System.out.println("Done");
	}
	
	public SecureLoader(Person user,SolrSession session) {
		this.user = user;
		this.session = session;
	}
	
	public Person loadPerson(SolrDocument profile) {
		
		//Load up the most basic of people
		Person person = new Person();
		person.setUid((String)profile.getFieldValue("uid"));
		person.setCredentials(SerialUtils.loadStringSet((String)profile.getFieldValue("credentials")));
		
		//Any further queries required should be executed as under this person.
		SolrSession personSession = session.solr.newSession(person);
		
		String permissions = (String)profile.getFieldValue("permissions");
		String[] parts = permissions.split(":");
		for(int j=0; j<parts.length-1; j+=2) {
			
			if(!isApproved(user,parts[j+1]) || parts[j].equals("uid") || parts[j].equals("credentials"))
				continue;
			
			String fieldname = parts[j];
			Object fieldvalue = profile.getFieldValue(fieldname);
			
			person.setFieldFromRawValue(fieldname,fieldvalue,permissions,personSession);
		}

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
}
