package gov.nysenate.opendirectory.solr;

import java.util.TreeSet;

import org.apache.solr.common.SolrDocument;

import gov.nysenate.opendirectory.models.Person;

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
		//Do the loading here
		Person person = new Person();
		
		//long start = System.nanoTime();
		String permissions = (String)profile.getFieldValue("permissions");
		String[] parts = permissions.split(":");
		for(int j=0; j<parts.length-1; j+=2) {
			
			if(!isApproved(user,parts[j+1]))
				continue;
			
			String fieldname = parts[j];
			Object fieldvalue = profile.getFieldValue(fieldname);
			
			person.setFieldFromRawValue(fieldname,fieldvalue,permissions,session);
		}
		//long end = System.nanoTime();
		//System.out.println((end-start)/1000f+" microsecond load");
		
		System.out.println("Num Bookmarks: "+person.getBookmarks().size());
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
