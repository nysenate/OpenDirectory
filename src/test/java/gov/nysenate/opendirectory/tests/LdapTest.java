package gov.nysenate.opendirectory.tests;

import java.util.Collection;

import javax.naming.NamingException;

import junit.framework.Assert;
import junit.framework.TestCase;
import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;

public class LdapTest extends TestCase {
	/*
	public LdapTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	*/
	
	public void testConnectAnon() {
		Assert.fail("We fail");
		Assert.assertTrue(false);
		try {
			new Ldap().connect();
		} catch (NamingException e) {
			Assert.fail(e.toString());
		}
	}
	
	public void testConnectAuth() {
		
	}
	
	public void testGetPersonByName() {
		try {
			Ldap ldap = new Ldap().connect();
			Collection<Person> people = ldap.getPersonByName("Jared*");
			for(Person p : people) {
				System.out.println(p);
			}
		} catch (NamingException e) {
			Assert.fail(e.toString());
		}
	}
}
