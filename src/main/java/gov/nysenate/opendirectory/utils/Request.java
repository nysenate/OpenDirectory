package gov.nysenate.opendirectory.utils;

import java.io.IOException;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.BaseServlet;
import gov.nysenate.opendirectory.solr.SolrSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Request {

	public Person user;
	
	public HttpSession httpSession;
	public SolrSession solrSession;
	
	public HttpServletRequest httpRequest;
	public HttpServletResponse httpResponse;
	public BaseServlet servlet;
	
	public Request(BaseServlet servlet, HttpServletRequest request,HttpServletResponse response) {
		this.servlet = servlet;
		this.httpRequest = request;
		this.httpResponse = response;
		
		httpSession = request.getSession(true);
		
		String uid = (String)httpSession.getAttribute("uid");
		if( uid != null) {
			user = servlet.solrServer.newSession(Person.getAdmin()).loadPersonByUid(uid);
			
			//Bad uids should be removed from the session
			if(user == null)
				httpSession.setAttribute("uid", null);
		}
		
		//If no user was specified/found, log them as the Anonymous user.
		if( user == null)
			user = Person.getAnon();
		
		solrSession = servlet.solrServer.newSession(user);
		
		//Set up request defaults
		request.setAttribute("title", "NYSS OpenDirectory");
	}
	
	public void render(String name) throws IOException, ServletException {
		if(!user.equals(Person.getAnon()))
			httpRequest.setAttribute("user",user);
		
		httpRequest.setAttribute("urls",servlet.urls);
		servlet.getServletContext().getRequestDispatcher("/jsp/"+name).forward(httpRequest, httpResponse);
	}
	
	public void redirect(String name) throws IOException, ServletException {
		httpResponse.sendRedirect(name);
	}
}
