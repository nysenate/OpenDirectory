package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

@SuppressWarnings("serial")
public class SolrControllerServlet extends BaseServlet {
	
	public static void main(String[] args) {
		try {
			Collection<Person> people = new SolrControllerServlet().replaceDepartments(new Ldap().connect().getPeople());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		try {
			ServletOutputStream out = response.getOutputStream();
			String command = urls.getCommand(request);
		    if (command != null) {
		    	if(command.equals("removeAll")) {
		    		removeAll(self);
		    		out.println("Removed all documents");
		    	} else if (command.equals("reindexAll")) {
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
		Collection<Person> people = replaceDepartments(new Ldap().connect().getPeople());
		HashMap<String,Person> solrPeople = getUidPersonMap(self.solrSession.loadPeople());
		ArrayList<Person> toAdd = new ArrayList<Person>();
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
				toAdd.add(solrPerson);
				solrPeople.remove(solrPerson.getUid());
			}
			else {
				toAdd.add(ldapPerson);
			}
			
			
		}
		self.solrSession.savePeople(toAdd);
		
		//no longer in ldap
		for(String uid:solrPeople.keySet()) {
			self.solrSession.deleteByUid(uid);
		}
	}
	private Collection<Person> replaceDepartments(Collection<Person> people) {
		for(Person p : people) {
			String department = p.getDepartment();
			if(department==null || department.isEmpty()) {
				continue;
			}
			
			department = department.replaceAll("ARRC", "Agriculture and Rural Resources");
			department = department.replaceAll("dev","Development");
			department = department.replaceAll("DO","District Office");
			department = department.replaceAll("M&O", "Maintenance and Operations");
			department = department.replaceAll("CS","Creative Services");
			department = department.replaceAll("LC","Legislative Committee");
			department = department.replaceAll("Maj\\.", "Majority");
			department = department.replaceAll("Prog\\.", "Program");
			department = department.replaceAll("SC", "Select Committee");
			department = department.replaceAll("SS", "Senate Services");
			department = department.replaceAll("STS", "Senate Technology Services");
			department = department.replaceAll("Sess\\. Asst\\.", "Session Assistant");
			department = department.replaceAll("TF", "Task Force");
			
			department = department.replaceAll("^Senator","Senators/Senator");
			
			if(department.equals("NYS Black PR Hisp & Asian Leg Cau")) {
				department = "NYD Black, Puerto Rican, Hispanic & Legislative Caucus";
			}
			else if(department.equals("Task Force/Demographic Research & Reapp.")) {
				department = "Task Force/Demographic Research & Reapportionment";
			}
			
			if(department.contains("Caucus")) {
				department = "Caucuses/" + department;
			}
			
			p.setDepartment(department);
		}
		return people;
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
