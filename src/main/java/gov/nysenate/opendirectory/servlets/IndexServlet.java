package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.FrontPagePeople;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		request.setAttribute("frontPagePeople", ((FrontPagePeople)request.getSession().getAttribute("frontPagePeople")).getFrontPagePeople(self));
		self.render("index.jsp");
	}
	
}
