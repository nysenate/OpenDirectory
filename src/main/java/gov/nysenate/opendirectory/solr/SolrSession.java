package gov.nysenate.opendirectory.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.SerialUtils;

public class SolrSession {

	Solr solr;
	Person user;
	SecureLoader loader;
	SecureWriter writer;
	
	@SuppressWarnings("serial")
	public class SolrSessionException extends Exception {
		public SolrSessionException(String m) { super(); }
		public SolrSessionException(String m ,Throwable t) { super(m,t); }
	}
	
	public static void main(String[] args) throws SolrServerException, IOException {
		
//		SolrSession solr = new SolrSession(Person.getAnon(),new Solr().connect());
//		
//		for(int i=0; i<10; i++)
//			solr.loadPeople();
		
		Solr solr = new Solr().connect();
		SolrSession session = solr.newSession(Person.getAdmin());
		
		Person me = session.loadPersonByUid("williams");
		
		System.out.println(me.getPicture().substring(me.getPicture().lastIndexOf(".")));
	}
	
	public SolrSession(Person user, Solr solr) {
		this.solr = solr;
		this.user = user;
		this.loader = new SecureLoader(user,this);
		this.writer = new SecureWriter(user,this);
	}
	
	public Person loadPersonByUid(String uid) {
		
		//Do the query on the uid field
		ArrayList<Person> people = loadPeopleByQuery("uid:"+uid);
		
		//Return null on no results, sometimes getResults returns null
		if(people.isEmpty() == true) {
			return null;
			
		//Load a person from the profile if 1 result
		} else if ( people.size() == 1) {
			return people.get(0);
			
		//This should never happen since uid is unique in the SOLR config file.
		} else { return null; } //TODO: this should throw an exception! 
		//throw new SolrSessionException("UID provided ("+uid+") was not unique in solr!");
	}
	
	public ArrayList<Person> loadPeopleByQuery(String query) {
		return loadPeopleBySortedQuery(query,null,false);
	}
	
	public ArrayList<Person> loadSortedPeople(String sortField, boolean asc) {
		//Use the otype field to locate all person documents
		return loadPeopleBySortedQuery("otype:person", sortField, asc);
	}
	
	public ArrayList<Person> loadPeople() {
		//Use the otype field to locate all person documents
		return loadPeopleBySortedQuery("otype:person", null, false);
	}
	
	public ArrayList<Person> loadPeopleBySortedQuery(String query, String sortField, boolean asc) {
		
		String creds = SerialUtils.writeStringSet(user.getCredentials(),", ");		
		
		query = queryParser(query,creds);
		
		System.out.println("\nLoading People By Query: "+query);
		System.out.println(creds);
		System.out.println("===============================================");
		
		//Execute the query
		long start = System.nanoTime();
		
		QueryResponse results = null;
		if(sortField != null) {
			results = solr.sortedQuery(query, 2000, sortField, asc);
		}
		else {
			results = solr.query(query,2000);
		}
		
		if(results==null)
			return new ArrayList<Person>();
		
		SolrDocumentList profiles = results.getResults();
		System.out.println((System.nanoTime()-start)/1000000f+" ms - query to solr");
		
		if(profiles==null)
			return new ArrayList<Person>();
		
		//Transform the results
		start = System.nanoTime();
		ArrayList<Person> people = new ArrayList<Person>();
		for( SolrDocument profile : profiles ) {
			people.add(loader.loadPerson(profile));
		}
		
		System.out.println((System.nanoTime()-start)/1000000f+" ms - load Person array");
		
		return people;
		
	}
	
	public void savePerson(Person person) throws SolrServerException, IOException {
		addPerson(person);
		solr.server.commit();
	}
	
	public void savePeople(Collection<Person> people)  throws SolrServerException, IOException  {
		for(Person person : people)
			addPerson(person);
		solr.server.commit();
	}
	
	public void optimize() throws SolrServerException, IOException {
		solr.server.optimize();
	}
	
	public void deleteAll(){
		try {
			solr.deleteAll();
			solr.server.commit();
		} catch (SolrServerException e) {
			//TODO: this should throw a SolrSessionException
		} catch (IOException e) {
			//TODO: this should throw a SolrSessionException
		}
	}
	
	public void deleteByUid(String uid) {
		try {
			solr.delete("uid:"+uid);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	private void addPerson(Person person) throws SolrServerException, IOException {
		solr.server.add(writer.writePerson(person));
	}
	
	public String queryParser(String query, String creds) {
		String aon = "(?i:and|or|not)";
		
		TreeSet<String> basics = new TreeSet<String>(Arrays.asList("uid","otype","firstName","lastName","fullName"));
		Pattern p = Pattern.compile("(.*?)(\\w+):(\\(.+?\\)|\\[.+?\\]|.+?)([\\*~])?(\\s" + aon + "|" + aon + "\\s|\\s" + aon + "\\s|$)");
		Matcher m = p.matcher(query);
		if(m.find()) {
			query ="";
			do {
				String quant = (m.group(4) != null && !m.group(4).equals("") ? m.group(4) : "");
				boolean paren = (m.group(3).startsWith("(") ? false:true );
				if(basics.contains(m.group(2)))
					query += m.group(1)+m.group(2)+":"+m.group(3) + quant;
				else
					query += m.group(1)+"("+m.group(2)+":"+(paren ? "(" + m.group(3) + ")" : m.group(3)) + quant +" AND "+m.group(2)+"_access:("+creds+"))"+m.group(5);
			} while(m.find());
		}
		//can't find <term>:<param> formatted query, most likely free input
		else {
			query = "uid:(" 			+ query + ")" +
					"OR firstName:("	+ query + ")" +
					"OR lastName:(" 	+ query + ")" +
					"OR fullName:("	 	+ query + ")" +
					" OR (title:("	 	+ query + ") AND title_access:(" 		+ creds + "))" +
					" OR (state:("	 	+ query + ") AND state_access:(" 		+ creds + "))" +
					" OR (location:(" 	+ query + ") AND location_access:(" 	+ creds + "))" +
					" OR (department:(" + query + ") AND department_access:("	+ creds + "))" +
					" OR (phone:(" 		+ query + ") AND phone_access:(" 		+ creds + "))" +
					" OR (email:(" 		+ query + ") AND email_access:(" 		+ creds + "))" +
					" OR (skills:(" 	+ query + ") AND skills_access:(" 		+ creds + "))" +
					" OR (interests:(" 	+ query + ") AND interests_access:(" 	+ creds + "))" +
					" OR (bio:(" 		+ query + ") AND bio_access:(" 			+ creds + "))" +
					" OR (facebook:(" 	+ query + ") AND facebook_access:(" 	+ creds + "))" +
					" OR (linkedin:(" 	+ query + ") AND linkedin_access:(" 	+ creds + "))" +
					" OR (twitter:(" 	+ query + ") AND twitter_access:(" 		+ creds + "))" +
					" OR (irc:(" 		+ query + ") AND irc_access:(" 			+ creds + "))" +
					" OR (email2:(" 	+ query + ") AND email2_access:(" 		+ creds + "))" +
					" OR (phone2:(" 	+ query + ") AND phone2_access:(" 		+ creds + "))";
		}
		return query;
	}
}
