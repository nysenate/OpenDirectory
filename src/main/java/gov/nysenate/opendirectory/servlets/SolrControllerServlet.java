package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;

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
		    		indexAll(self);
		    		out.println("Indexed all Documents");
		    	} else if (command.equals("reindexAll")) {
		    		removeAll(self);
		    		indexAll(self);
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
	
	private void removeAll(Request self) throws SolrServerException, IOException {
		self.solrSession.deleteAll();
	}
	
	private void indexAll(Request self) throws SolrServerException, IOException  {
		try{
			self.solrSession.savePeople(new Ldap().connect().getPeople());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
