package gov.nysenate.opendirectory.ldap;

import gov.nysenate.opendirectory.models.Person;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
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
	 */
	public static void main(String[] args) throws NamingException {
	
		try {				
			
			/*******PULL in INFO from LDAP*********/
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
			
			/*****Create solr server connection********/
			CommonsHttpSolrServer localserver = null;
			localserver = new CommonsHttpSolrServer("http://localhost:8080/solr/");
			
			/*
			
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
			
			start = System.currentTimeMillis();
			
			SolrServer server = localserver;
			
			
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			query.setRows(1900);
			
			QueryResponse rsp = server.query(query);
			
			System.out.println("Querying takes: "+(System.currentTimeMillis()-start)+" milliseconds");
			
			System.out.println(rsp.getResults().size());
			
			/*
			
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
			
		} 
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
		} 
		
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
