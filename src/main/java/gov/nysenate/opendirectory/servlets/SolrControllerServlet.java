package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

@SuppressWarnings("serial")
public class SolrControllerServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		try {
			ServletOutputStream out = response.getOutputStream();
			String command = urls.getCommand(request);
		    if (command != null) {
		    	if(command.equals("removeAll")) {
		    		removeAll(self);
		    		out.println("Removed all documents");
		    	} else if (command.equals("indexAll")) {
		    		indexLdap(self, out);
		    		indexExtras(self);
		    		out.println("Indexed all Documents");
		    	} else if (command.equals("indexExtras")) {
		    		indexExtras(self);
		    		out.println("Indexed all Extras");
		    	} else if (command.equals("reindexAll")) {
		    		//removeAll(self);
		    		//indexLdap(self);
		    		reindexAll(self);
		    		indexExtras(self);
		    		out.println("Removed and Reindexed All documents");
		    	} else {
		    		out.println("Unknown command: "+command);
		    		out.println("Recognized Commands are: removeAll,indexAll, and reindexAll");
		    	}
		    } else {
		    	out.println("Available commands are: ");
		    	out.println("\tRemoveAll - /solr/removeAll");
		    	out.println("\tIndexAll - /solr/indexAll");
		    	out.println("\tReindexAll - /solr/reindexAll");
		    }
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void reindexAll(Request self) throws NamingException, SolrServerException, IOException {
		Collection<Person> people = new Ldap().connect().getPeople();
		HashMap<String,Person> solrPeople = getUidPersonMap(self.solrSession.loadPeople());

		for(Person ldapPerson: people) {
			Person solrPerson = solrPeople.get(ldapPerson.getUid());
			
			//pretty awful merge
			if(solrPerson != null) {
				solrPerson.setDepartment(ldapPerson.getDepartment());
				solrPerson.setEmail(ldapPerson.getEmail());
				solrPerson.setFirstName(ldapPerson.getFirstName());
				solrPerson.setLastName(ldapPerson.getLastName());
				solrPerson.setLocation(ldapPerson.getLocation());
				solrPerson.setPhone(ldapPerson.getPhone());
				solrPerson.setState(ldapPerson.getState());
				solrPerson.setTitle(ldapPerson.getTitle());
				solrPerson.setUid(ldapPerson.getUid());
				
				self.solrSession.deleteByUid(solrPerson.getUid());
				self.solrSession.savePerson(solrPerson);
				solrPeople.remove(solrPerson.getUid());
			}
			else {
				self.solrSession.savePerson(ldapPerson);
			}
			
		}
		
		//no longer in ldap
		for(String uid:solrPeople.keySet()) {
			self.solrSession.deleteByUid(uid);
		}
	}
	
	private HashMap<String,Person> getUidPersonMap(Collection<Person> people) {
		HashMap<String,Person> map = new HashMap<String,Person>();
		for(Person person:people) {
			map.put(person.getUid(), person);
		}
		return map;
	}
	
	private void removeAll(Request self) throws SolrServerException, IOException {
		self.solrSession.deleteAll();
	}
	
	private void indexLdap(Request self, ServletOutputStream out) throws SolrServerException, IOException  {
		try{
			out.println("getting people");
			Collection<Person> people = new Ldap().connect().getPeople();
			out.println("saving people");
			self.solrSession.savePeople(people);
			self.solrSession.optimize();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	private void indexExtras(Request self) throws SolrServerException, IOException {
		
		Person opendirectory = new Person();
		opendirectory.setUid("opendirectory");
		opendirectory.setFirstName("SenBook");
		opendirectory.setFullName("OpenDirectory");
		opendirectory.setEmail("opendirectory@nysenate.gov");
		opendirectory.setTitle("New Age Techno Contact Service");
		opendirectory.setDepartment("Office of the CIO");
		opendirectory.setPhone("1-866-OPENDIR");
		opendirectory.setLocation("Big Back Office, Agency 4");
		opendirectory.setBio("Origionally code named SenBook, the project was then renamed OpenDirectory and development began in the Java Servlets Environment as part of an RPI capstone course.");
		opendirectory.setSkills(new TreeSet<String>(Arrays.asList("Java","Solr","Ldap","Varnish")));
		opendirectory.setInterests(new TreeSet<String>(Arrays.asList("Python","Open Source")));
		
		opendirectory.setPermissions(Person.getDefaultPermissions());
		opendirectory.setCredentials(new TreeSet<String>(Arrays.asList("public","senate")));
		self.solrSession.savePerson(opendirectory);
		
		Person chrim = new Person();
		chrim.setUid("chrim");
		chrim.setFirstName("Chris");
		chrim.setLastName("Kim");
		chrim.setFullName("Chris Kim");
		self.solrSession.savePerson(chrim);
		
		Person chrib = new Person();
		chrib.setUid("chrib");
		chrib.setFirstName("Chris");
		chrib.setLastName("Babie");
		chrib.setFullName("Chris Babie");
		self.solrSession.savePerson(chrib);
		
		Person graylin = new Person();
		graylin.setUid("graylin");
		graylin.setFirstName("Graylin");
		graylin.setLastName("Kim");
		graylin.setFullName("Graylin Kim");
		self.solrSession.savePerson(graylin);
		
		self.solrSession.optimize();
	}
}
