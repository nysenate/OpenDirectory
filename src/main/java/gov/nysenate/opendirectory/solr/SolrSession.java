package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.solr.analysis.KeywordTokenizerFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
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
	
	public ArrayList<Person> loadPeople() {
		
		//Do the query
		QueryResponse results = solr.query("*:*");
		SolrDocumentList profiles = results.getResults();
		
		//Transform the results
		ArrayList<Person> people = new ArrayList<Person>();
		for( SolrDocument profile : profiles ) {
			people.add(loader.loadPerson(profile));
		}
		return people;
	}
	
	//Could use addBean function that comes with solrj but we need to 
	//put the credentials (hashmap) into solr field.
	private void addPerson(Person person) throws SolrServerException, IOException {
		SolrInputDocument solr_person = new SolrInputDocument();
		String permissions = Permissions(person.getPermissions());
		String credentials = person.getCredentials().toString().substring(1, person.getCredentials().toString().length()-1);
			
		solr_person.addField("otype", "person", 1.0f);
		solr_person.addField("firstName", person.getFirstName(), 1.0f);
		solr_person.addField("lastName", person.getLastName(), 1.0f);
		solr_person.addField("title", person.getTitle(), 1.0f);
		solr_person.addField("id", person.getUid(), 1.0f);
		solr_person.addField("fullName", person.getFullName(), 1.0f);
		solr_person.addField("state", person.getState(), 1.0f);
		solr_person.addField("location", person.getLocation(), 1.0f);
		solr_person.addField("department", person.getDepartment(), 1.0f);
		solr_person.addField("phone", person.getPhone(), 1.0f);
		solr_person.addField("email", person.getEmail(), 1.0f);
		solr_person.addField("permissions", permissions);
		solr_person.addField("user_credential", credentials);
		
		System.out.println(permissions);
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
	
/*	To be figured out... need to figure out annotations with Graylin
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
		credentials="<fields>\n";
		
		String key;
		String credential_list;
		String temp;
		
		while(permission.hasNext())
		{
			key = permission.next().toString();
			credential_list = permissions.get(key).toString();
			temp = credential_list.substring(1, credential_list.length()- 1);
			
			credentials+="<field name=\"" + key + "\" allow = \"" + 
				temp + "\"/>\n"; 
			
		}
		

		credentials+="</fields>";
		return credentials;
		
	}
}
