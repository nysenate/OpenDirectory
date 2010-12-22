package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SearchServlet extends BaseServlet {

	public class SearchServletException extends Exception {
		public SearchServletException(String m) { super(m); }
		public SearchServletException(String m, Throwable t) { super(m,t); }
	}
	
	public static void main(String[] args) {

	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		
		try {
			String query = request.getParameter("query");
			if(query == null)
				throw new SearchServletException("No query supplied with the search request.");
			
			ArrayList<Person> people = self.solrSession.loadPeopleByQuery(query);
			
			if(people.isEmpty()) {
				Pattern pattern = Pattern.compile("(\\w+?):(\\w+?)(\\s(AND|OR)|$)");
				Matcher matcher = pattern.matcher(query);
				if(!matcher.find()) {
					query = query + "*";
					people = self.solrSession.loadPeopleByQuery(query + "*");
					if(people.isEmpty()) {
						people = self.solrSession.loadPeopleByQuery(query + "~");
					}
				}
				else {
					query = matcher.replaceAll("$1:$2*$3");
					people = self.solrSession.loadPeopleByQuery(matcher.replaceAll("$1:$2*$3"));
					
					if(people.isEmpty()) {
						query = matcher.replaceAll("$1:$2~$3");
						people = self.solrSession.loadPeopleByQuery(matcher.replaceAll("$1:$2~$3"));
					}
				}
			}
			
			request.setAttribute("query", query);
			request.setAttribute("results", people);
			
			self.render("search.jsp");
			
		} catch (SearchServletException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			if(e.getCause()!=null)
				e.getCause().printStackTrace();
			
			// TODO this should also redirect to somewhere sensible
		}
	}
	
}
