package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.servlets.utils.BaseServlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class APIServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String version = urls.getCommand(request);
	    if (version != null) {
	    	if(version.equals("1.0")) {
	    		Vector<String> args = urls.getArgs(request);
		    	if(!args.isEmpty()) {
		    		//String command = args.remove(0);
		    		//Finish this at some point
		    		
		    	} else {
		    		//Do some sort of return about the options, instructions or w/e
		    	}
	    	} else {
	    		//List the versions that are appropriate
	    	}
	    } else {
	    	//Give some sort of documentation
	    }
	    
	}
}
