package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.utils.Request;


import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BrowseServlet extends BaseServlet {
	// TODO This bucketting could probably be done in a much better way
	public class BrowseServletException extends Exception {
		public BrowseServletException(String m) { super(m); }
		public BrowseServletException(String m, Throwable t) { super(m,t); }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		try {
			try {
				String command = urls.getCommand(request);
				System.out.println(command);
			    if (command != null) {
			    	if ( command.equals("department")) {						
			    		request.setAttribute("self", self);
						self.render("dept.jsp");
						
			    	} else if ( command.equals("firstname") ) {
						
			    		request.setAttribute("self", self);
			    		self.render("first.jsp");
						
			    	} else if ( command.equals("lastname") ) {
			    		
			    		request.setAttribute("self", self);
						self.render("last.jsp");
			    		
			    	} else if ( command.equals("location") ) {
						
			    		request.setAttribute("self", self);
						self.render("loc.jsp");
						
			    	}
			    	
			    } else {
			    	self.render("index.jsp");
			    }
			} catch (SecurityException e) {
				throw new BrowseServletException("Somehow the BrowseServlet does not have permissions to access the getMethods.");
			}
			
		} catch (BrowseServletException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			if(e.getCause()!=null)
				e.getCause().printStackTrace();
			// TODO something reasonable to resolve things here
		}
		
	}
}