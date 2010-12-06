package gov.nysenate.opendirectory.tests;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.solr.Solr;
import gov.nysenate.opendirectory.solr.SolrSession;
import junit.framework.TestCase;

public class SolrSessionTest extends TestCase {

	private Solr solrServer;
	
	public SolrSessionTest(String name) {
		super(name);
		
		solrServer = new Solr().connect();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testConstructor() {
		
		//When no user is passed in, a default user should be generated
		solrServer.newSession(null);

		//When no user is passed in, a default user should be generated
		solrServer.newSession(Person.getAdmin());
	}
	
	public void testGetPersonByUid() {
		SolrSession session = solrServer.newSession(Person.getAnon());
		
		//Person who exists
		assertNotNull(session.loadPersonByUid("opendirectory"));
		
		//Person who doesn't exist
		assertNull(session.loadPersonByUid("opendir"));
		
	}

}
