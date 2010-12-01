package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PersonServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String uid = urls.getCommand(request);
	    if(uid != null) {
	    	Vector<String> args = urls.getArgs(request);
	    	Person person = self.solrSession.loadPersonByUid(uid);
	    	if(args.size()==1 && args.get(0).equals("vcard.vcf")) { 
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
	    	}
	    	else if (args.isEmpty()) {
		    	try {
			    	request.setAttribute("person", person);
				    self.render("profile.jsp");
		    	} catch (NullPointerException e) {
		    		
		    	}
	    	}
	    } else {
	    	//No UID supplied so...
	    }
	}
}