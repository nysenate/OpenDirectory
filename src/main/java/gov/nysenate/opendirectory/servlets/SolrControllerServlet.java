package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.Solr;
import gov.nysenate.opendirectory.solr.SolrSession;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

@SuppressWarnings("serial")
public class SolrControllerServlet extends HttpServlet {
	
	private Solr solrServer;
	
	public SolrControllerServlet() {
		System.out.println("Entering the constructor");
		this.solrServer = new Solr().connect();
		System.out.println("Exiting the constructor");
	}
	
	public static void main(String[] args) {
		System.out.println("Hello");
		new SolrControllerServlet();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Hello");
		SolrSession solr = solrServer.newSession(new Person());
		System.out.println("We got a new server session");
		try {
			ServletOutputStream out = response.getOutputStream();
			StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
			tokens.nextToken(); //Throw `OpenDirectory` away
		    tokens.nextToken(); //Throw `solr` away
		    if (tokens.hasMoreTokens()) {
		    	String command = tokens.nextToken();
		    	if(command.equals("removeAll")) {
		    		removeAll(solr);
		    		out.println("Removed all documents");
		    	} else if (command.equals("indexAll")) {
		    		indexAll(solr);
		    		out.println("Indexed all Documents");
		    	} else if (command.equals("reindexAll")) {
		    		removeAll(solr);
		    		indexAll(solr);
		    		out.println("Remove and Reindexed All documents");
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
	
	private void removeAll(SolrSession solr) throws SolrServerException, IOException {
		solr.deleteAll();
	}
	
	private void indexAll(SolrSession solr) throws SolrServerException, IOException  {
		try{
			solr.savePeople(new Ldap().connect().getPeople());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
}
