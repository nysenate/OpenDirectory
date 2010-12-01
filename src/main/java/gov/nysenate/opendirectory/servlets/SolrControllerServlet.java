package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.Arrays;
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
		    		indexLdap(self);
		    		indexExtras(self);
		    		out.println("Indexed all Documents");
		    	} else if (command.equals("indexExtras")) {
		    		indexExtras(self);
		    		out.println("Indexed all Extras");
		    	} else if (command.equals("reindexAll")) {
		    		removeAll(self);
		    		indexLdap(self);
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
		}
	}
	
	private void removeAll(Request self) throws SolrServerException, IOException {
		self.solrSession.deleteAll();
	}
	
	private void indexLdap(Request self) throws SolrServerException, IOException  {
		try{
			self.solrSession.savePeople(new Ldap().connect().getPeople());
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
		self.solrSession.optimize();
	}
}
