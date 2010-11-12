package gov.nysenate.opendirectory.servlets.utils;

import javax.servlet.http.HttpServlet;

import gov.nysenate.opendirectory.solr.Solr;

@SuppressWarnings("serial")
abstract public class BaseServlet extends HttpServlet {
	
	protected UrlMapper urls;
	protected Solr solrServer;
	
	public BaseServlet() {
		urls = new UrlMapper();
		this.solrServer = new Solr().connect();
	}
}
