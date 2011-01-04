package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.utils.FrontPagePeople;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class IndexServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		request.setAttribute("frontPagePeople", ((FrontPagePeople)self.httpSession.getAttribute("frontPagePeople")).getFrontPagePeople(self));
		self.render("index.jsp");
	}
	
}
