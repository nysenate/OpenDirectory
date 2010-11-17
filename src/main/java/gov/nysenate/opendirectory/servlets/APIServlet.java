package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class APIServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = response.getOutputStream();
		Request self = new Request(this,request,response);
		String version = urls.getCommand(request);
	    if (version != null) {
	    	if(version.equals("1.0")) {
	    		doVersion1_0(self,urls.getArgs(request),out);
	    	} else {
	    		out.println("Version must be 1.0 for now");
	    	}
	    } else {
	    	out.println("Only valid command right now is:");
	    	out.println("\t api/person/uid/<uid>.<format>");
	    }
	}
	
	public void doVersion1_0(Request self, ArrayList<String> args, ServletOutputStream out) throws IOException {
		for(String arg : args)
			System.out.println(arg);
		if(!args.isEmpty()) {
    		String command = args.remove(0);
    		if(command.equals("person")) {
    			if(!args.isEmpty()) {
    				String method = args.remove(0);
    				if(method.equals("uid")) {
    					if(!args.isEmpty()) {
    						String whole = args.remove(0);
    						System.out.println("Requested person id: "+whole);
    						String[] parts = whole.split("\\.");
    						if(parts.length == 2) {
	    						String uid = parts[0];
	    						String format = parts[1];
	    						Person person = self.solrSession.loadPersonByUid(uid);
	    						if(format.equals("xml")) {
	    							out.println(person.toString());
	    						} else {
	    							out.println("Format currently unrecognized");
	    						}
    						} else {
    							out.println("Bad format for the <id>.<format>, parts length: "+parts.length);
    						}
    					} else {
    						out.println("Command requires the uid be supplied");
    					}
    				} else {
    					out.println("Person retrieval method not yet supported");
    				}
    			} else {
    				//They need to specify a method
    			}
    		} else {
    			//Handle other cases
    		}
    	} else {
    		//Do some sort of return about the options, instructions or w/e
    	}
	}
}
