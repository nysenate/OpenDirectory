package gov.nysenate.opendirectory.solr;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.solr.common.SolrInputDocument;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.SerialUtils;

public class SecureWriter {

	Person user;
	SolrSession session;
	
	public static void main(String[] args) {

	}
	
	public SecureWriter(Person user, SolrSession session) {
		this.user = user;
		this.session = session;
	}

	public SolrInputDocument writePerson(Person person) {
		//Writes the person to a SolrDocument
		SolrInputDocument doc = new SolrInputDocument();
		HashMap<String,TreeSet<String>> permissions = person.getPermissions();
		
		//Open LDAP Information
		doc.addField("otype", "person", 1.0f);
		doc.addField("uid", person.getUid(), 1.0f);
		doc.addField("firstName", person.getFirstName(), 1.0f);
		doc.addField("lastName", person.getLastName(), 1.0f);
		doc.addField("fullName", person.getFullName(), 1.0f);
		
		//Secure LDAP Information
		writeSecureField(doc,"title",person.getTitle(), permissions, 1.0f);
		writeSecureField(doc,"location",person.getLocation(),permissions, 1.0f);
		writeSecureField(doc,"department",person.getDepartment(),permissions, 1.0f);
		writeSecureField(doc,"phone",person.getPhone(),permissions, 1.0f);
		writeSecureField(doc,"email",person.getEmail(),permissions, 1.0f);
		
		//Secure Account Information
		writeSecureField(doc,"bookmarks",SerialUtils.writeBookmarks(person.getBookmarks()),permissions,1.0f);
		writeSecureField(doc,"user_credential", SerialUtils.writeStringSet(person.getCredentials(),", "),permissions,1.0f);
		
		//Secure User Information
		writeSecureField(doc,"phone2",person.getPhone2(),permissions, 1.0f);
		writeSecureField(doc,"email2",person.getEmail2(),permissions, 1.0f);
		writeSecureField(doc,"bio",person.getBio(),permissions, 1.0f);
		writeSecureField(doc, "unprocessedBio", person.getUnprocessedBio(),permissions, 1.0f);
		writeSecureField(doc,"picture",person.getPicture(),permissions, 1.0f);
		writeSecureField(doc,"twitter",person.getTwitter(),permissions, 1.0f);
		writeSecureField(doc,"facebook",person.getFacebook(),permissions, 1.0f);
		writeSecureField(doc,"linkedin",person.getLinkedin(),permissions, 1.0f);
		writeSecureField(doc,"irc",person.getIrc(),permissions, 1.0f);
		writeSecureField(doc,"skills",SerialUtils.writeStringSet(person.getSkills(),", "),permissions, 1.0f);
		writeSecureField(doc,"interests",SerialUtils.writeStringSet(person.getInterests(),", "),permissions, 1.0f);
		
		doc.addField("modified", new Date().getTime());
		doc.addField("frontPage", person.getFrontPage() ? "true":"false");

		//I think state is probably useless
		//writeSecureField(doc,"state", person.getState(),permissions, 1.0f);
		
		//doc.addField("permissions", SerialUtils.writeSetHash(person.getPermissions()));
		return doc;
	}
	
	public void writeSecureField(SolrInputDocument doc, String name, String value, HashMap<String,TreeSet<String>> permissions, float boost) {
		//Add the field
		doc.addField(name, value, boost);
		
		//Always add the admin permissions to everything
		TreeSet<String> field_permissions = permissions.get(name);
//		field_permissions.add("admin");
		
		//Add a permissions field for it with no impact on search ranking
		//(I'm not actually sure that's what boost does, we should look it up.
		doc.addField(name+"_access", SerialUtils.writeStringSet(field_permissions,", "), 0f);
	}
}
