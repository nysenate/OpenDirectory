package gov.nysenate.opendirectory.ldap;

import gov.nysenate.opendirectory.models.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Ldap {

	DirContext context;
	String cred;
	String pwd;
	
	public Ldap() { context = null; }

	public Ldap connect() throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		
		//Required options
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,"ldap://webmail.nysenate.gov");
		
		//Create the LDAP context from the environment
		context = new InitialDirContext(env);
		return this;
	}
	
	public Ldap connect(String cred, String pwd) throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		
		//Required options
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,"ldap://webmail.nysenate.gov");
		
		//If they supplied user name and password set options for authentication
		if(cred != null && pwd != null) {
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL,cred);
			env.put(Context.SECURITY_CREDENTIALS,pwd);
		} else {
			throw new NamingException("Credentials and Password are required");
		}
		
		//Create the LDAP context from the environment
		context = new InitialDirContext(env);
		return this;
	}
	
	public Collection<Person> getPeople() throws NamingException {
		if (context == null)
			throw new NamingException("Must connect to server before querying");

		NamingEnumeration<SearchResult> results = context.search(
					"O=senate",
					"(objectClass=dominoPerson)",
					new SearchControls()
				);
		
		ArrayList<Person> ret = new ArrayList<Person>();
		while(results.hasMore()) {
			ret.add(new Person(results.next()));
		}
		return ret;
	}
	public Collection<Person> getPersonByName(String name) throws NamingException {
		if (context == null)
			throw new NamingException("Must connect to server before querying");

		NamingEnumeration<SearchResult> results = context.search(
					"O=senate",
					"(cn="+name+")",
					new SearchControls()
				);
		
		ArrayList<Person> ret = new ArrayList<Person>();
		while(results.hasMore()) {
			ret.add(new Person(results.next()));
		}
		return ret;
	}
}
/*
	try {
		//create the default set of Search Controls
		SearchControls controls = new SearchControls();
		
		//Connection credentials (null,null) = anonymous
		String cred = null;
		String pass = null;
		
		//The DirContext represents our connection with ldap
		DirContext ldap = getLdap(cred,pass);
		
		//Set up the search filters. LDAP will apply the searchFilter within the domain specified
		String domainFilter = "O=senate"; 			//organization = senate
		String searchFilter = "(givenname=Jared)"; 	//first name = Jared

		//Execute our search over the `O=senate` ldap domain
		//with the `givenname=Jared` query and default controls  
		NamingEnumeration<SearchResult> results = ldap.search(domainFilter,searchFilter,controls);
		
		//Iterate through our results
		int resultNum = 1;
		System.out.println("Results for query: `"+searchFilter+"`");
		while (results.hasMore()) {
			SearchResult result = results.next();
			
			//Get the result attributes and all of their IDs
			Attributes attributes = result.getAttributes();
			NamingEnumeration<String> ids = result.getAttributes().getIDs();
			
			//Iterate through our attributes
			System.out.println("Result "+resultNum+": "+result.getName());
			while( ids.hasMore() ) {
				String id = ids.next();
				//Get all the values for that attribute (could be a list)
				NamingEnumeration<?> values = attributes.get(id).getAll();
				
				//Iterate through those values
				StringBuilder row = new StringBuilder("\t").append(id).append(": ");
				while(values.hasMore()) {
					row.append(values.next());
					if (values.hasMore()) {
						row.append(", ");
					}
				}
				
				System.out.println(row);
			}
			
			resultNum++;
		}
	
	//If the authorization credentials are bad, we'll catch that here and report the failure
	} catch (AuthenticationException e) {
		System.out.println("Authentication Failed!");
	}
	
	//Bad queries aren't caught here, not sure how to do that yet.
}
	
}
*/