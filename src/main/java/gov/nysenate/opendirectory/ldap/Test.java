package gov.nysenate.opendirectory.ldap;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.SolrSession;
import gov.nysenate.opendirectory.solr.Solr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.beans.*;

public class Test {
	
	/**
	 * @param args
	 * @throws NamingException
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	
	public static void main(String[] args) throws NamingException, SolrServerException, IOException {

		//try {				
			
			//Test secureloader
			Solr test_solr = new Solr();
			test_solr.connect();
			
			SolrSession test_session = new SolrSession(Person.getAdmin(), test_solr);
			Person result = new Person();
			result = test_session.loadPersonByName("Jared\\ Williams");
			TreeSet<String> fullname= new TreeSet<String>();
			fullname.add("Jared Chausow");
			result.getBookmarks().put("chausow", fullname);
			
//			//result.
			//result.setSkills(skills);
			
			//System.out.println(result.getPermissions().get("uid"));
			//test_solr.delete("Jared\\ Williams");
			
			System.out.println("deleted");
			
			test_session.savePerson(result);
			
			result = test_session.loadPersonByName("Jared\\ Williams");
			
			System.out.println(result.getLastName());
			System.out.println(result.getLocation());
			System.out.println(result.getEmail());
			System.out.println(result.getDepartment());
			System.out.println(result.getFullName());
			System.out.println(result.getFirstName());
			System.out.println(result.getPermissions());
			System.out.println(result.getSkills());
			System.out.println(result.getBookmarks());
			
			/*SolrQuery query = new SolrQuery();
			query.setQuery("id:codetes*");
			query.setRows(1);
			
			QueryResponse rsp = server.query(query);
			*/
			
			/*/TEST INPUTTING A PERSON INTO SOLR
			Solr test_solr = new Solr();
			test_solr.connect();
			
			Person test_person = new Person();

			TreeSet<String> credentials = new TreeSet<String>();
			HashMap<String,TreeSet<String>> permission = new HashMap<String,TreeSet<String>>();
			
			credentials.add("alpha_private");
			credentials.add("bravo_public");
			//Have dept and email have alpha, bravo credentials
			permission.put("department", new TreeSet<String>(credentials));
			permission.put("email", new TreeSet<String>(credentials));
			
			//Have first and lastname have alpha, bravo, and charlie credentials
			credentials.add("charlie_senate");
			permission.put("firstName", new TreeSet<String>(credentials));
			permission.put("lastName", new TreeSet<String>(credentials));
			
			//Have location have alpha, bravo, charlie, delta, and echo credentials
			credentials.add("delta_admin");
			credentials.add("echo_GODMODE");
			permission.put("location", new TreeSet<String>(credentials));
			permission.put("fullName", new TreeSet<String>(credentials));
			
			credentials.add("foxtrot_senatorA");
			
			
			test_person.setUid("codetest");
			test_person.setDepartment("CODETEST_department");
			test_person.setEmail("CODETEST_email");
			test_person.setFirstName("CODETEST_first");
			test_person.setLastName("CODETEST_last");
			test_person.setLocation("CODETEST_location");
			test_person.setFullName("codetestname");
			test_person.setCredentials(new TreeSet<String>(credentials));
			test_person.setPermissions(permission);
			
			SolrSession test_session = new SolrSession(test_person, test_solr);
			
			test_session.savePerson(test_person);	
			
			
			/*******PULL in INFO from LDAP*********
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
			//String searchFilter = "(displayname=Andrew H*)"; 
			//String searchFilter = "(uid=CRM*)"; //all "CRM" users
			//String searchFilter = "(&(objectClass=dominoGroup)(giddisplay=Public))";
			String searchFilter = "(&(objectClass=dominoPerson)(employeeid=*)(!(employeeid=999*))(!(employeeid=0000)))"; //clean query
			
			long start = System.currentTimeMillis();
			//Execute our search over the `O=senate` ldap domain 
			NamingEnumeration<SearchResult> results = ldap.search(domainFilter,searchFilter,controls);
			System.out.println("Searching takes: "+(System.currentTimeMillis()-start)+" milliseconds");
			
			/*****Create solr server connection********
			CommonsHttpSolrServer localserver = null;
			localserver = new CommonsHttpSolrServer("http://localhost:8080/solr/");
			
			*/
			
			/*********TIMING
			start = System.currentTimeMillis();
			SolrServer server = localserver;
			server.deleteByQuery("*:*");
			System.out.println("Deletion takes: "+(System.currentTimeMillis()-start)+" milliseconds");
		
			//Iterate through our results, create Person objects, add them to solr
			System.out.println("Results for query: `"+searchFilter+"`");
			ArrayList<Person> people_to_add = new ArrayList();
			int resultNum=1;
			while (results.hasMore()) {
				System.out.println(resultNum++);
				people_to_add.add(new Person(results.next()));
			}
			
			server.addBeans(people_to_add);
			server.commit();
			*/
			
			/*****TESTING for TIMING OF QUERIES
			start = System.currentTimeMillis();
			
			SolrServer server = localserver;
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			query.setRows(1900);
			
			QueryResponse rsp = server.query(query);
			
			System.out.println("Querying takes: "+(System.currentTimeMillis()-start)+" milliseconds");
			
			System.out.println(rsp.getResults().size());
			*/
			
			/*****READING RESULTS FROM LDAP
			//Get the result attributes and all of their IDs
			//Attributes attributes = result.getAttributes();
			//NamingEnumeration<String> ids = result.getAttributes().getIDs();
			
			//Iterate through our attributes
			//System.out.println("Result "+resultNum+": "+result.getName());
			while( ids.hasMore() ) {
				
				String id = ids.next();
				//Get all the values for that attribute (could be a list)
				NamingEnumeration<?> values = attributes.get(id).getAll();
				
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
			*/
		
			
		/*}
		//If the authorization credentials are bad, we'll catch that here and report the failure
		catch (AuthenticationException e) {
			System.out.println("Authentication Failed!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Bad queries aren't caught here, not sure how to do that yet.
	}
	

	private static SolrServer getSolrServer() {
		// TODO Auto-generated method stub
		return null;
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
