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

import gov.nysenate.opendirectory.models.ExternalPerson;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.models.interfaces.IPerson;
import gov.nysenate.opendirectory.utils.BCrypt;
import gov.nysenate.opendirectory.utils.SerialUtils;

public class SolrSession {

	Solr solr;
	IPerson user;
	SecureLoader loader;
	SecureWriter writer;
	
	@SuppressWarnings("serial")
	public class SolrSessionException extends Exception {
		public SolrSessionException(String m) { super(); }
		public SolrSessionException(String m ,Throwable t) { super(m,t); }
	}
	
	public static void main(String[] args) throws SolrServerException, IOException {
		
		Solr solr = new Solr().connect();
		SolrSession session = solr.newSession(Person.getAdmin());
	}
	
	public SolrSession(IPerson user, Solr solr) {
		this.solr = solr;
		this.user = user;
		this.loader = new SecureLoader(user,this);
		this.writer = new SecureWriter(user,this);
	}
	
	public ExternalPerson loadExternalPersonByEmail(String email) {
		String query = "otype:externalPerson AND uid:" + email;
		QueryResponse results = solr.query(query);
		if(results == null)
			return null;
		
		SolrDocumentList profiles = results.getResults();
		if(profiles == null || profiles.isEmpty())
			return null;
		
		ExternalPerson person = loader.loadExternalPerson(profiles.iterator().next());
		if(!person.getEmail().equals(email)) 
			return null;
		
		return person;
	}
	
	public Person loadPersonByUid(String uid) {
		
		//Do the query on the uid field
		ArrayList<Person> people = loadPeopleByQuery("uid:"+uid, false);
		
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
	
	public ArrayList<Person> loadPeopleByQuery(String query, boolean fuzz) {
		return loadPeopleBySortedQuery(query,null,false, fuzz);
	}
	
	public ArrayList<Person> loadSortedPeople(String sortField, boolean asc, boolean fuzz) {
		//Use the otype field to locate all person documents
		return loadPeopleBySortedQuery("otype:person", sortField, asc, fuzz);
	}
	
	public ArrayList<Person> loadPeople() {
		//Use the otype field to locate all person documents
		return loadPeopleBySortedQuery("otype:person", null, false, false);
	}
	
	public ArrayList<Person> loadPeopleBySortedQuery(String query, String sortField, boolean asc, boolean fuzz) {
		
		String creds = SerialUtils.writeStringSet(user.getCredentials(),", ");		
				
		System.out.println("\nLoading People By Query: "+query);
		System.out.println(creds);
		System.out.println("===============================================");
		
		//Execute the query
		long start = System.nanoTime();
		
		QueryResponse results = null;
		results = solr.sortedQuery(personQueryParser(query,creds, "(\"", "\")"), 2000, sortField, asc);
		
		if(fuzz && (results == null || results.getResults().isEmpty())) {
			results = solr.sortedQuery(personQueryParser(
					query,creds, "(", ")"), 2000, sortField, asc);
			
			if(results == null || results.getResults().isEmpty()) {
				Pattern pattern = Pattern.compile("(\\w+?):(\\w+?)(\\s(AND|OR)|$)");
				Matcher matcher = pattern.matcher(query);
				if(!matcher.find()) {
					results = solr.sortedQuery(personQueryParser(
							query + "*",creds, "(", ")"), 2000, sortField, asc);

					if(results == null || results.getResults().isEmpty()) {
						results = solr.sortedQuery(personQueryParser(
								query + "~",creds, "(", ")"), 2000, sortField, asc);
					}
				}
				else {
					results =solr.sortedQuery(personQueryParser(
							matcher.replaceAll("$1:$2*$3"), creds, "(", ")"), 2000, sortField, asc);
					
					if(results == null || results.getResults().isEmpty()) {
						results = solr.sortedQuery(personQueryParser(
								matcher.replaceAll("$1:$2~$3"), creds, "(", ")"), 2000, sortField, asc);
					}
				}
			}		
		}
		
		if(results == null)
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
	
	public void saveExternalPerson(ExternalPerson person) throws SolrServerException, IOException {
		addExternalPerson(person);
		solr.server.commit();
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
	
	private void addExternalPerson(ExternalPerson person) throws SolrServerException, IOException {
		solr.server.add(writer.writeExternalPerson(person));
	}
	
	private void addPerson(Person person) throws SolrServerException, IOException {
		solr.server.add(writer.writePerson(person));
	}
	
	public String personQueryParser(String query, String creds, String wrpL, String wrpR) {		
		creds = creds + "*";
		String aon = "(?i:and|or|not)";
		
		TreeSet<String> basics = new TreeSet<String>(Arrays.asList("uid","otype","firstName","lastName","fullName", "frontPage"));
		Pattern p = Pattern.compile("(.*?)(\\w+):(\\(.+?\\)|\\[.+?\\]|.+?)([\\*~])?(\\s" + aon + "|" + aon + "\\s|\\s" + aon + "\\s|$)");
		Matcher m = p.matcher(query);
		if(m.find()) {
			query ="";
			for(int i = 0;i <= m.groupCount();i++) { System.out.println(i + ": " + m.group(i));}
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
			//these are used to wrap the default search, sometimes ([term]) may be desired, sometimes ("[term]")
			wrpL = wrpL == null ? "(" : wrpL;
			wrpR = wrpR == null ? ")" : wrpR;
			query = "uid:"				+ wrpL + query + wrpR +
					" OR firstName:"	+ wrpL + query + wrpR +
					" OR lastName:"		+ wrpL + query + wrpR +
					" OR fullName:"		+ wrpL + query + wrpR +
					" OR (title:"		+ wrpL + query + wrpR + " AND title_access:(" 		+ creds + "))" +
					" OR (state:"		+ wrpL + query + wrpR + " AND state_access:(" 		+ creds + "))" +
					" OR (location:"	+ wrpL + query + wrpR + " AND location_access:(" 	+ creds + "))" +
					" OR (department:"	+ wrpL + query + wrpR + " AND department_access:("	+ creds + "))" +
					" OR (phone:"		+ wrpL + query + wrpR + " AND phone_access:(" 		+ creds + "))" +
					" OR (email:"		+ wrpL + query + wrpR + " AND email_access:(" 		+ creds + "))" +
					" OR (skills:"		+ wrpL + query + wrpR + " AND skills_access:(" 		+ creds + "))" +
					" OR (interests:"	+ wrpL + query + wrpR + " AND interests_access:(" 	+ creds + "))" +
					" OR (bio:"			+ wrpL + query + wrpR + " AND bio_access:(" 		+ creds + "))" +
					" OR (facebook:"	+ wrpL + query + wrpR + " AND facebook_access:(" 	+ creds + "))" +
					" OR (linkedin:"	+ wrpL + query + wrpR + " AND linkedin_access:(" 	+ creds + "))" +
					" OR (twitter:"		+ wrpL + query + wrpR + " AND twitter_access:(" 	+ creds + "))" +
					" OR (irc:"			+ wrpL + query + wrpR + " AND irc_access:(" 		+ creds + "))" +
					" OR (email2:"		+ wrpL + query + wrpR + " AND email2_access:(" 		+ creds + "))" +
					" OR (phone2:"		+ wrpL + query + wrpR + " AND phone2_access:(" 		+ creds + "))";
		}
		System.out.println("otype:person AND " + query.replaceFirst("otype:.*?[\\s\\)\\]]", ""));
		return "otype:person AND (" + query.replaceFirst("otype:.*?[\\s\\)\\]]", "") + ")";
	}
}
