package gov.nysenate.opendirectory.servlets;

import java.io.IOException;
import java.util.StringTokenizer;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@SuppressWarnings("serial")
public class BrowseServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
		tokens.nextToken(); //Throw `OpenDirectory` away
	    tokens.nextToken(); //Throw `browse` away
	    if (tokens.hasMoreTokens()) {
	    	String name = tokens.nextToken().toLowerCase();
	    	if ( name.equals("department")) {
	    		getServletContext().getRequestDispatcher("/dept.jsp").forward(request, response);
	    	} else if ( name.equals("firstname") ) {
	    		getServletContext().getRequestDispatcher("/first.jsp").forward(request, response);
	    	} else if ( name.equals("lastname") ) {
	    		getServletContext().getRequestDispatcher("/last.jsp").forward(request, response);
	    	} else if ( name.equals("location") ) {
	    		getServletContext().getRequestDispatcher("/loc.jsp").forward(request, response);
	    	}
	    } else {
	    	getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	    }
	}
}