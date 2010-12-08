package gov.nysenate.opendirectory.ldap;

import gov.nysenate.opendirectory.models.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.TreeSet;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
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
	
	public static boolean authenticate(String cred, String pwd) throws NamingException {
		
		try {
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
			
			if(pwd.isEmpty())
				throw new NamingException("A password is required to log in!");
			
			new InitialDirContext(env);
			return true;
		} catch (AuthenticationException e) {
			return false;
		}
		
	}
	
	public Collection<Person> getPeople() throws NamingException {
		if (context == null)
			throw new NamingException("Must connect to server before querying");

		NamingEnumeration<SearchResult> results = context.search(
					"",
					"(&(objectClass=dominoPerson)(employeeid=*)(!(employeeid=999*))(!(employeeid=0000)))",
					new SearchControls()
				);
		
		ArrayList<Person> ret = new ArrayList<Person>();
		while(results.hasMore()) {
			ret.add( loadPerson(results.next()) );
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
			ret.add( loadPerson(results.next()) );
		}
		return ret;
	}
	public Collection<Person> getPersonByUid(String name) throws NamingException {
		if (context == null)
			throw new NamingException("Must connect to server before querying");

		NamingEnumeration<SearchResult> results = context.search(
					"O=senate",
					"(uid="+name+")",
					new SearchControls()
				);
		
		ArrayList<Person> ret = new ArrayList<Person>();
		while(results.hasMore()) {
			ret.add( loadPerson(results.next()) );
		}
		return ret;
	}
	
	private String getAttribute(Attributes attributes,String name) throws NamingException {
		Attribute attr = attributes.get(name);
		
		if(attr != null && attr.size() != 0)
			return (String)attr.get();
		else
			return null;
	}
	
	public Person loadPerson(SearchResult record) throws NamingException {
		Person person = new Person();
		
		//Copy non-internal/payroll LDAP values into the person object 
		Attributes attributes = record.getAttributes();
		person.setEmail(getAttribute(attributes,"mail"));
		person.setPhone(getAttribute(attributes,"telephonenumber"));
		person.setState(getAttribute(attributes,"st"));
		person.setDepartment(getAttribute(attributes,"department"));
		person.setTitle(getAttribute(attributes,"title"));
		person.setFirstName(getAttribute(attributes,"givenname"));
		person.setLastName(getAttribute(attributes,"sn"));
		person.setUid(getAttribute(attributes,"uid"));
		person.setLocation(getAttribute(attributes,"l"));
		
		//Get the full name (w/ suffix and mi) and fix it up for display
		String fullName = getAttribute(attributes,"displayname");
		if (fullName!=null)
			fullName = fullName.split("/")[0];
		person.setFullName(fullName);
		
		//Override default permissions to mark as a senate member (public and senate permissions)
		person.setCredentials(new TreeSet<String>(Arrays.asList("senate","public")));
		
		return person;
	}
}