package gov.nysenate.opendirectory.servlets;

import javax.servlet.http.HttpServlet;

import gov.nysenate.opendirectory.solr.Solr;
import gov.nysenate.opendirectory.utils.UrlMapper;

@SuppressWarnings("serial")
abstract public class BaseServlet extends HttpServlet {
	
	public UrlMapper urls;
	public Solr solrServer;
	
	public BaseServlet() {
		urls = new UrlMapper();
		this.solrServer = new Solr().connect();
	}
}
