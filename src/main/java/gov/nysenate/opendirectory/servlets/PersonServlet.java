package gov.nysenate.opendirectory.servlets;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PersonServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
	    StringTokenizer tokens = new StringTokenizer(request.getRequestURI(),"/");
	    tokens.nextToken(); //Throw `OpenDirectory` away
	    
	    if(tokens.hasMoreTokens()) {
	    	//String name = tokens.nextToken().toLowerCase();
	    	
	    } else {
	    	
	    }
	    
	    request.setAttribute("extra", request.getRequestURI());
	    getServletContext().getRequestDispatcher("/testing.jsp").forward(request, response);
	    */
	}
}