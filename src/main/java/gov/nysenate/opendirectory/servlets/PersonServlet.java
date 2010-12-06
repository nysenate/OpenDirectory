package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class PersonServlet extends BaseServlet {

	public class PersonServletException extends Exception {
		public PersonServletException(String m) { super(m); }
		public PersonServletException(String m, Throwable t) { super(m,t); }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String uid = urls.getCommand(request);
	    
	    try {
		    if(uid == null)
		    	throw new PersonServletException("No uid was supplied");
		    
		    Person person = self.solrSession.loadPersonByUid(uid);
		    if(person==null)
		    	throw new PersonServletException("Bad uid was supplied");
		    
	    	Vector<String> args = urls.getArgs(request);
	    	if(args.size()==0)
	    		throw new PersonServletException("Not no command was supplied");
	    	
	    	String command = args.get(0);
	    	if(command.equals("profile")) {
		    	request.setAttribute("person", person);
		    	self.render("profile.jsp");
		    	
	    	} else if (command.equals("vcard")) {
	    		// TODO vcard needs to get fixed up, maybe Jared can do this?
	    		ServletOutputStream out = response.getOutputStream();
	    		StringBuilder mResult = new StringBuilder();
				mResult.append("BEGIN:VCARD\r\n");
				mResult.append("VERSION:2.1");
				mResult.append("\r\nN:").append(person.getLastName()).append(";").append(person.getFirstName()).append(";;;");
				mResult.append("\r\nFN:").append(person.getFullName());
				mResult.append("\r\nTEL;TYPE=WORK:").append(person.getPhone());
				mResult.append("\r\nEMAIL;TYPE=PREF;TYPE=INTERNET:").append(person.getEmail());
				mResult.append("\r\nEND:VCARD\r\n");
				out.print(mResult.toString());
				
	    	} else
	    		throw new PersonServletException("Invalid command `"+command+"` was supplied.");
	    	
	    } catch (PersonServletException e) {
	    	
	    }
	    
	}
}