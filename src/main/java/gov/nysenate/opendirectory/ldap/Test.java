package gov.nysenate.opendirectory.ldap;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class Test {
	/**
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException {
		
		try {
			
			//Set the attributes to retrieve
			String[] attributestoretrieve = {"displayname","location","givenname","uidnumber",
											"uid", "mail", "cn", "telephonenumber",
											"st","l","sn","department", "title","gidnumber", "employeeid"};
			
			//create the default set of Search Controls
			SearchControls controls = new SearchControls();
			controls.setReturningAttributes(attributestoretrieve);
			
			//Connection credentials (null,null) = anonymous
			String cred = null;
			String pass = null;
			
			//The DirContext represents our connection with ldap
			DirContext ldap = getLdap(cred,pass);
			
			//Set up the search filters. LDAP will apply the searchFilter within the domain specified
			String domainFilter = "O=senate"; 			//organization = senate
			//String domainFilter = new String();
			//String searchFilter = "(objectClass=dominoPerson)"; 	//all users
			//String searchFilter = "(uidnumber=1087)"; 
			String searchFilter = "(uid=CRM*)"; //all "CRM" users
			//String searchFilter = "(giddisplay=*)";
			//String searchFilter = "(&(objectClass=dominoGroup)(giddisplay=Public))";
			//String searchFilter = "(&(objectClass=dominoPerson)(employeeid=*)(!(employeeid=999*))(!(employeeid=0000)))"; //clean query
			
			//Execute our search over the `O=senate` ldap domain 
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
					
					//Print Results
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
	

	public static DirContext getLdap(String cred, String pwd) throws NamingException {
		Hashtable<String,String> env = new Hashtable<String,String>();
		
		//Required options
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,"ldap://webmail.nysenate.gov");
		
		//If they supplied user name and password set options for authentication
		if(cred != null && pwd != null) {
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL,cred);
			env.put(Context.SECURITY_CREDENTIALS,pwd);
		}
		
		//Create the LDAP context from the environment
		return new InitialDirContext(env);
	}
}
