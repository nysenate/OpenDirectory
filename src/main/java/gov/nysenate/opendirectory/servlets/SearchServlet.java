package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchServlet extends BaseServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		String query = request.getParameter("query");
		if(query != null) {
			ArrayList<Person> results = self.solrSession.loadPeopleByQuery(query);
			System.out.println(results.size());
			for(Person result : results) {
				System.out.println(result.getFirstName());
			}
			request.setAttribute("query", query);
			request.setAttribute("results", results );
		}
		
		self.render("/search.jsp");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Request self = new Request(this,request,response);
		System.out.println("Sent a post request by accident");
	}
}
