package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AutoSuggestServlet extends BaseServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		String command = urls.getCommand(request);
		
		String term = (String)self.httpRequest.getParameter("term");
		
		PrintWriter out = self.httpResponse.getWriter();
				
		if(command.equals("search")) {
			out.print(doSearchSuggest(self, term));
		}
		else if(command.equals("skills")) {
			String set = (String)self.httpRequest.getParameter("set");
			out.println(doSkillsOrInterestsSuggest(self, command, term, set));
		}
		else if(command.equals("interests")) {
			String set = (String)self.httpRequest.getParameter("set");
			out.print(doSkillsOrInterestsSuggest(self, command, term, set));
		}
	}
	
	public String doSkillsOrInterestsSuggest(Request self, String command, String term, String current) {				
		ArrayList<String> set = new ArrayList<String> (Arrays.asList(current.toLowerCase().split(",")));
		ArrayList<String> suggestionSet = new ArrayList<String>();
		
		String query =  command + ":(" + term + "~) OR " + command + ":(" + term + "*) OR " + command + ":(" + term + ")";
				
		ArrayList<Person> people = self.solrSession.loadPeopleByQuery(query, false);
				
		for(Person person:people) {
			
			TreeSet<String> personSet = (command.equals("skills") ? person.getSkills() : person.getInterests());
			
			for(String value:personSet) {
				if(value.toLowerCase().startsWith(term.toLowerCase())) {
					if(!suggestionSet.contains(value.toLowerCase()) && !set.contains(value.toLowerCase())) {
						suggestionSet.add(value.toLowerCase());
					}
				}
			}
			
			if(suggestionSet.size() >= 5) {
				break;
			}
		}
		
		String html = "";
		
		for(int i = 0; i < suggestionSet.size(); i++) {
			if(i == 0) {
				html += "<li id=\"selected_suggestion\" class=\"suggestions_box\">";
			}
			else {
				html += "<li class=\"suggestions_box\">";
			}
			html += suggestionSet.get(i) + "</li>";
		}
		return html;
	}
	
	
	public String doSearchSuggest(Request self, String term) {
		ArrayList<Person> people = self.solrSession.loadPeopleByQuery(term, true);
		int max = people.size() >= 10 ? 10 : people.size();
		
		String html = "";
		html += "<li><em>" + people.size() + " total results... (<a href=\"/opendirectory/search/?query="
				+ term + "\">view all</a>)</em></li>";
		for(int i = 0; i < max; i++) {
			Person person = people.get(i);
			html += "<li class=\"quickresult_box\"><a href=\"/" + 
				urls.url("person", person.getUid(),"profile") + "\" class=\"sublink\">" +
				person.getFirstName() + " " + person.getLastName() + 
					(person.getDepartment() != null && !person.getDepartment().equals("") ?
							" - " + person.getDepartment() : "") +
				"</a></li>";
		}
		return html;
	}
}
