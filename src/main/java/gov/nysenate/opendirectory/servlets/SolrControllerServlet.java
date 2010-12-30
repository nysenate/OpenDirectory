package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.Solr;
import gov.nysenate.opendirectory.solr.SolrSession;
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
		if(!self.user.getUid().equals("williams")) {
			self.render(urls.url("index"));
		}
		else {
			try {
				ServletOutputStream out = response.getOutputStream();
				String command = urls.getCommand(request);
			    if (command != null) {
			    	if(command.equals("removeAll")) {
			    		removeAll(self);
			    		out.println("Removed all documents");
			    	} else if (command.equals("reindexAll")) {
			    		reindexAll(self);
			    		out.println("Reindexed all ldap values");
			    	} 
			    	else if (command.equals("resetPermissions")){
			    		resetPermissions(self);
			    		out.println("Reset all permissions");

			    	}
			    	else {
			    		out.println("Unknown command: "+command);
			    		out.println("Recognized Commands are: removeAll,reindexAll, and resetPermissions");
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
	}
	
	public void resetPermissions(Request self) throws NamingException, SolrServerException, IOException {
		Solr solr = new Solr().connect();
		SolrSession session = new SolrSession(Person.getAdmin(),solr);
		Collection<Person> people = session.loadPeople();
		ArrayList<Person> toAdd = new ArrayList<Person>();
		for(Person ldapPerson: people) {
			
			ldapPerson.setPermissions(ldapPerson.getPermissions());
			ldapPerson.setCredentials(null);
			toAdd.add(ldapPerson);
			
		}
		self.solrSession.savePeople(toAdd);
	}
	
	private void reindexAll(Request self) throws NamingException, SolrServerException, IOException {
		Collection<Person> people = replaceDepartments(new Ldap().connect().getPeople());
		
		Solr solr = new Solr().connect();
		SolrSession session = new SolrSession(Person.getAdmin(),solr);
		
		HashMap<String,Person> solrPeople = getUidPersonMap(session.loadPeople());
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
			
			if(department.equals("Senate Technology Services")) {
				department = "Senate Technology Services/STS General";
			}
			
			if(department.contains("Senatorial District")) {
				department = "Senators/" + department;
			}
			
			if(department.equals("NYS Black  PR  Hisp & Asian Leg Cau")) {
				department = "NYS Black, Puerto Rican, Hispanic & Legislative Caucus";
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
	}
}
