package gov.nysenate.opendirectory.solr;

import java.util.HashMap;
import java.util.TreeSet;

import org.apache.solr.common.SolrDocument;

import gov.nysenate.opendirectory.models.ExternalPerson;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.models.interfaces.IPerson;
import gov.nysenate.opendirectory.utils.SerialUtils;
public class SecureLoader {

	private IPerson user;
	private SolrSession session;
	
	public static void main(String[] args) {
		SolrSession session = new Solr().connect().newSession(Person.getAdmin());
		session.loadPersonByUid("opendirectory");
		System.out.println("Done");
	}
	
	public SecureLoader(IPerson user,SolrSession session) {
		this.user = user;
		this.session = session;
	}
	
	public ExternalPerson loadExternalPerson(SolrDocument profile) {
		ExternalPerson person = new ExternalPerson();
				
		person.setEmail((String)profile.getFieldValue("uid"));
		person.setFirstName((String)profile.getFieldValue("firstName"));
		person.setLastName((String)profile.getFieldValue("lastName"));
		person.setPhone((String)profile.getFieldValue("phone"));
		person.setHash((String)profile.getFieldValue("hash"));
		person.setAuthorized(new Boolean((String)profile.getFieldValue("authorized")));
		person.setAuthorizationHash((String)profile.getFieldValue("authorizationHash"));
		
		SerialUtils.loadStringSet((String)profile.getFieldValue("user_credential"),", ");
		
		return person;
	}
	
	public Person loadPerson(SolrDocument profile) {
		
		//Load up the person in a basic way
		Person person = new Person(
				(String)profile.getFieldValue("uid"),
				SerialUtils.loadStringSet((String)profile.getFieldValue("credentials"),", ")
			);
		
		//Any further queries required should be executed as under this person.
		SolrSession personSession = session.solr.newSession(person);
		HashMap<String,TreeSet<String>> permissions = person.getPermissions();
		
		String bookmarks = null;
		
		for(String field : profile.getFieldNames()) {
			//Process the permissions fields into the hashmap
			if(field.endsWith("_access")) {
				permissions.put(
						field.substring(0, field.length()-7),
						SerialUtils.loadStringSet((String)profile.getFieldValue(field),", ")
					);
				continue;
				
			}
				
			if( field.equals("uid") || field.equals("credentials") ||		 //We already have these, otherwise
				!isApproved((String)profile.getFieldValue(field+"_access"))) //User must have permissions to view
					continue;
			if(field.equals("bookmarks")) {
				bookmarks = field;
				continue;
			}
			
			
			person.loadField(field,profile.getFieldValue(field),personSession);
		}
		
		if(bookmarks != null) {
			person.loadField(bookmarks, profile.getFieldValue(bookmarks), personSession);
		}
		
		//We've now got a fully reconstructed permissions field
		person.setPermissions(permissions);
		
		return person;
	}

	private boolean isApproved(String permissions) {
		//If no permissions are set, its assumed public
		if(permissions==null)
			return true;
		
		//Auto-grant access to the admin user
		if(user.equals(Person.getAdmin()))
			return true;
		
        //This is the TreeSet that will house credentials for each field
        TreeSet<String> user_credentials = user.getCredentials();
        
        //Break the permissions up and check for matches
		for(String temp : SerialUtils.loadStringSet(permissions,", "))
			if(user_credentials.contains(temp) == true)
    			return true;
		
		return false;
	}
}
