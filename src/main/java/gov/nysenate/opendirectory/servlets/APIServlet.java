package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.solr.Solr;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class APIServlet extends HttpServlet {
	
	private Solr solrServer;
	
	public APIServlet() {
		//Only executed on startup
		solrServer = new Solr().connect();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `opendirectory` away
	    tokens.nextToken(); //Throw `user` away
	    
	    if (tokens.hasMoreTokens()) {
	    	String version = tokens.nextToken();
	    	
	    	if(tokens.hasMoreTokens()) {
	    		String command = tokens.nextToken();
	    		
	    	} else {
	    		
	    	}
	    	
	    } else {
	    	
	    }
	}
}
