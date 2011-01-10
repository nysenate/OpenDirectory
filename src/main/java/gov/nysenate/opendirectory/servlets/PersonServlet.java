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
	    		self.redirect(urls.url("person",uid,"profile"));
	    		//args.add("profile"); //throw new PersonServletException("No command was supplied");
	    	else {
	    		String command = args.get(0);
		    	if(command.equals("profile")) {
			    	request.setAttribute("person", person);
			    	request.setAttribute("title", person.getFirstName()+" "+person.getLastName()+" | NYSS Open Directory");
			    	self.render("profile.jsp");
			    	
		    	} else if (command.equals("vcard")) {
		    		response.setContentType("text/x-vcard;charset=UTF-8");
		    		ServletOutputStream out = response.getOutputStream();
		    		StringBuilder mResult = new StringBuilder();
					mResult.append("BEGIN:VCARD\r\n");
					mResult.append("VERSION:2.1");
					mResult.append("\r\nN:").append(person.getLastName()).append(";").append(person.getFirstName()).append(";;;");
					mResult.append("\r\nFN:").append(person.getFullName());
					mResult.append("\r\nORG:").append("New York State Senate").append(";").append(person.getDepartment());
					mResult.append("\r\nTITLE:").append(person.getTitle());
					mResult.append("\r\nTEL;TYPE=WORK:").append(person.getPhone());
					mResult.append("\r\nTEL;TYPE=OTHER:").append(person.getPhone2());
					mResult.append("\r\nEMAIL;TYPE=WORK;TYPE=INTERNET;TYPE=PREF:").append(person.getEmail());
					mResult.append("\r\nitem1.EMAIL;TYPE=INTERNET:").append(person.getEmail2());
					mResult.append("\r\nitem1.X-ABLabel:").append("_$!<Other>!$_");
					mResult.append("\r\nitem2.URL:").append(person.getTwitter());
					mResult.append("\r\nitem2.X-ABLabel:").append("Twitter");
					mResult.append("\r\nitem3.URL;TYPE=LINKEDIN:").append(person.getLinkedin());
					mResult.append("\r\nitem3.X-ABLabel:").append("LinkedIn");
					mResult.append("\r\nitem4.URL;TYPE=FACEBOOK:").append(person.getFacebook());
					mResult.append("\r\nitem4.X-ABLabel:").append("Facebook");
					mResult.append("\r\nEND:VCARD\r\n");
					out.print(mResult.toString());
					
		    	} else
		    		throw new PersonServletException("Invalid command `"+command+"` was supplied.");
	    	}
	    	
	    } catch (PersonServletException e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	    	if(e.getCause()!=null)
	    		e.getCause().printStackTrace();
	    	self.redirect(urls.url("index"));
	    }
	    
	}
}