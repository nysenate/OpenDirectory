package gov.nysenate.opendirectory.solr;

import gov.nysenate.opendirectory.models.Person;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class Solr {
	
	protected SolrServer server;
	
	public Solr() {
		server = null;
	}

	public Solr connect() {
		try {
			//Could use EmbeddedSolrServer(), depends on how we are setting up the environments?
			server = (SolrServer)new CommonsHttpSolrServer("http://localhost:8080/solr/");
			return this;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public SolrSession newSession(Person user) {
		return new SolrSession(user,this);
	}

	public QueryResponse query(String term) {
		return query(term,Integer.MAX_VALUE);
	}
	
	public QueryResponse query(String term, int results) {
		try {
			SolrQuery query = new SolrQuery();
			query.setQuery(term);
			query.setRows(results);
			return server.query(query);

		} catch (SolrServerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Solr deleteAll() throws IOException, SolrServerException {
		server.deleteByQuery("*:*");
		return this;
	}
	
	/*
		public Solr delete(String query) throws IOException, SolrServerException {
			server.deleteByQuery(query);
			return this;
		}
	
		public QueryResponse getAll() throws SolrServerException {
			return get("*:*");
		}
		public QueryResponse get(String queryString) throws SolrServerException {
			return get(queryString,Integer.MAX_VALUE);
		}
		
		public QueryResponse get(String queryString,int results) throws SolrServerException {
			SolrQuery query = new SolrQuery();
			query.setQuery(queryString);
			query.setRows(results);
			return server.query(query);
		}
	*/
}