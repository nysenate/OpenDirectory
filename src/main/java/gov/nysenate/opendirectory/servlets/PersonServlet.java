package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PersonServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String uid = urls.getCommand(request);
	    if(uid != null) {
	    	try {
		    	request.setAttribute("person", self.solrSession.loadPersonByUid(uid));
			    self.render("profile.jsp");
	    	} catch (NullPointerException e) {
	    		
	    	}
	    } else {
	    	//No UID supplied so...
	    }
	}
}