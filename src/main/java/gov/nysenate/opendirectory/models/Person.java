package gov.nysenate.opendirectory.models;

import gov.nysenate.opendirectory.solr.SolrSession;
import gov.nysenate.opendirectory.utils.SerialUtils;
import gov.nysenate.opendirectory.utils.XmlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Person implements Comparable {
	/** 
	 * Designed according to JavaBean specs for use in the
	 * Java Expressions Language (EL) of JSP 2.0+
	**/
	
	//VARIABLES NEED TO BE EXACT STRING MATCHES OF SOLR SCHEMA.XML
	/*If you want to add variables, you also have to add in logic in 
	 * SolrSession.addPerson(), SecureLoader.loadPerson(), and Person.getDefaultPermissions()
	 * 
	 * If you want to add a Hashmap you also have to copy logic in 
	 * SolrSession.Permissions() and SolrSession.Bookmarks().
	 * Also, pay particular attention to SecureLoader.loadPerson() and 
	 * see the if statements regarding the fields "permission" and "bookmarks"
	 */
	
	private TreeSet<String> credentials;
	private HashMap<String,TreeSet<String>> permissions;
	private TreeSet<Person> bookmarks;
	
	private TreeSet<String> skills;
	private TreeSet<String> interests;
	
	private String firstName;
	private String lastName;
	private String title;
	private String uid;
	private String fullName;
	private String state;
	private String location;
	private String department;
	private String phone;
	private String email;
	
	//additional Contact info
	private String bio;
	private String picture;
	private String email2;
	private String phone2;
	private String twitter;
	private String facebook;
	private String linkedin;
	private String irc;
	
	public static void main(String[] args) {
		Person a = new Person();
		a.setUid("a");
		Person b = new Person();
		b.setUid("b");
		Person c = new Person();
		c.setUid("c");
		Person i = new Person();
		i.setUid("c");
		Person j = new Person();
		j.setUid("c");
		Person x = new Person();
		x.setUid("d");
		TreeSet<Person> set = new TreeSet<Person>(Arrays.asList(a,b,c,i,j));
		System.out.println(set.contains(x));
		set.remove(i);
		System.out.println(set);
	}
	
	public Person() { setToDefaults(); }
	public Person(String uid) { setToDefaults(); setUid(uid); }
	public Person(String uid, TreeSet<String> credentials) { setToDefaults(); setUid(uid); setCredentials(credentials); }
	
	public void setToDefaults() {
		setFirstName("");
		setLastName("");
		setTitle("");
		setUid("");
		setFullName("");
		setState("");
		setLocation("");
		setDepartment("");
		setPhone("");
		setEmail("");
		
		//Need to set NON-NULL!!! defaults for everything here
		setBio("");
		setEmail2("");
		setPhone2("");
		setTwitter("");
		setFacebook("");
		setLinkedin("");
		setIrc("");
		setSkills(new TreeSet<String>());
		setInterests(new TreeSet<String>());
		setBookmarks(new TreeSet<Person>());
		setPicture("");
		
		//All people must have permissions and credentials
		setPermissions(Person.getDefaultPermissions());
		setCredentials(null); //Forces Defaults
	}
	public int compareTo(Object p) {
		System.out.println("Comparing "+getUid()+" to "+Person.class.cast(p).getUid());
		return getUid().compareTo(Person.class.cast(p).getUid());
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getTitle() {
		return title;
	}
	public String getUid() {
		return uid;
	}
	public String getFullName() {
		return fullName;
	}
	public String getState() {
		return state;
	}
	public String getLocation() {
		return location;
	}
	public String getDepartment() {
		return department;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public HashMap<String, TreeSet<String>> getPermissions() {
		return permissions;
	}
	public TreeSet<String> getCredentials(){
		return credentials;
	}
	public String getBio(){
		return bio;
	}
	public String getPicture(){
		return picture;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public String getFacebook() {
		return facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public String getPhone2() {
		return phone2;
	}
	public String getEmail2() {
		return email2;
	}
	public TreeSet<String> getInterests() {
		return interests;
	}
	public TreeSet<String> getSkills() {
		return skills;
	}
	public String getIrc() {
		return irc;
	}
	public TreeSet<Person> getBookmarks() {
		return bookmarks;
	}
	
	public void setBookmarks(TreeSet<Person> bookmarks) {
		this.bookmarks = bookmarks;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPermissions (HashMap<String, TreeSet<String>> permissions){
		this.permissions = permissions;
	}
	public void setCredentials (TreeSet<String> credentials){
		//There must always be a valid set of credentials for every single person
		if (credentials == null || credentials.isEmpty() )
			credentials = new TreeSet<String>(Arrays.asList("public"));
		this.credentials = credentials;
	}
	public void setBio(String bio){
		this.bio = bio;
	}
	public void setPicture(String picture){
		this.picture = picture;
	}
	public void setSkills(TreeSet<String> skills) {
		this.skills = skills;
	}
	public void setInterests(TreeSet<String> interests) {
		this.interests = interests;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public void setIrc(String IRC) {
		this.irc = IRC;
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(fullName+"("+uid+")");
		out.append("\n\tTitle: "+title);
		out.append("\n\tLocation: "+location+", "+state);
		out.append("\n\tDepartment:"+department);
		out.append("\n\tPhone: "+phone);
		out.append("\n\tEmail: "+email);
		return out.toString();
	}

	public static class ByFirstName implements Comparator<Person> {
		public int compare(Person a, Person b) {
			int ret = a.firstName.compareToIgnoreCase(b.firstName);
			if (ret == 0) {
				ret = a.lastName.compareToIgnoreCase(b.lastName);
				if (ret == 0) {
					ret = a.department.compareToIgnoreCase(b.department);
					if (ret == 0) {
						ret = a.location.compareToIgnoreCase(b.location);
						if (ret == 0) {
							ret = a.uid.compareToIgnoreCase(b.uid);
						}}}}
			return ret;
		}
	}
	
	public static class ByLastName implements Comparator<Person> {
		public int compare(Person a, Person b) {
			int ret = a.lastName.compareToIgnoreCase(b.lastName);
			if (ret == 0) {
				ret = a.firstName.compareToIgnoreCase(b.firstName);
				if (ret == 0) {
					ret = a.department.compareToIgnoreCase(b.department);
					if (ret == 0) {
						ret = a.location.compareToIgnoreCase(b.location);
						if (ret == 0) {
							ret = a.uid.compareToIgnoreCase(b.uid);
						}}}}
			return ret;
		}
	}

	public static class ByLocation implements Comparator<Person> {
		public int compare(Person a, Person b) {
			int ret = a.location.compareToIgnoreCase(b.location);
			if (ret == 0) {
				ret = a.firstName.compareToIgnoreCase(b.firstName);
				if (ret == 0) {
					ret = a.lastName.compareToIgnoreCase(b.lastName);
					if (ret == 0) {
						ret = a.department.compareToIgnoreCase(b.department);
						if (ret == 0) {
							ret = a.uid.compareToIgnoreCase(b.uid);
						}}}}
			return ret;
				
		}
	}
	
	public static class ByDepartment implements Comparator<Person> {
		public int compare(Person a, Person b) {
			int ret = a.department.compareToIgnoreCase(b.department);
			if (ret == 0) {
				ret = a.firstName.compareToIgnoreCase(b.firstName);
				if (ret == 0) {
					ret = a.lastName.compareToIgnoreCase(b.lastName);
					if (ret == 0) {
						ret = a.location.compareToIgnoreCase(b.location);
						if (ret == 0) {
							ret = a.uid.compareToIgnoreCase(b.uid);
						}}}}
			return ret;
		}
	}
	
	static Person admin;
	static Person anon;
	
	public synchronized static Person getAdmin() {
		if(admin == null) {
			admin = new Person();
			admin.setFullName("Administrator");
			admin.setPermissions(new HashMap<String,TreeSet<String>>());
			admin.setCredentials(new TreeSet<String>(Arrays.asList("public","senate","admin")));	
		}
		return admin;
	}
	
	public synchronized static Person getAnon() {
		if(anon == null) {
			anon = new Person();
			anon.setFullName("Anonymous User");
			anon.setPermissions(new HashMap<String, TreeSet<String>>());
			anon.setCredentials(new TreeSet<String>(Arrays.asList("public")));
		}
		return anon;
	}
	
	public static HashMap<String,TreeSet<String>> getDefaultPermissions() {
		
		HashMap<String, TreeSet<String>> permissions = new HashMap<String,TreeSet<String>>();
		//for each field put in default permission
		permissions.put("permissions", new TreeSet<String>(Arrays.asList("admin")));
		permissions.put("user_credential", new TreeSet<String>(Arrays.asList("admin")));
		permissions.put("bookmarks", new TreeSet<String>(Arrays.asList("admin")));
		
		permissions.put("uid", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("email", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("phone", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("state", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("department", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("title", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("firstName", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("fullName", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("lastName", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("location", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("bio", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("picture", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("email2", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("phone2", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("twitter", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("facebook", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("linkedin", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("irc", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("skills", new TreeSet<String>(Arrays.asList("public")));
		permissions.put("interests", new TreeSet<String>(Arrays.asList("public")));
		
		return permissions;
	}
	
	public Document toXml() {
		Document xml = XmlUtils.getBuilder().newDocument();
		Element person = xml.createElement("person");
			XmlUtils.appendLeaf(xml,person,"uid",getUid());
			XmlUtils.appendLeaf(xml,person,"fullName",getFullName());
			XmlUtils.appendLeaf(xml,person,"firstName",getFirstName());
			XmlUtils.appendLeaf(xml,person,"lastName",getLastName());
			XmlUtils.appendLeaf(xml,person,"location",getLocation());
			XmlUtils.appendLeaf(xml,person,"department",getDepartment());
			XmlUtils.appendLeaf(xml,person,"phone",getPhone());
			XmlUtils.appendLeaf(xml,person,"phone2",getPhone2());
			XmlUtils.appendLeaf(xml,person,"email",getEmail());
			XmlUtils.appendLeaf(xml,person,"email2",getEmail2());
			XmlUtils.appendLeaf(xml,person,"title",getTitle());
			XmlUtils.appendLeaf(xml,person,"irc",getIrc());
			XmlUtils.appendLeaf(xml,person,"twitter",getTwitter());
			XmlUtils.appendLeaf(xml,person,"facebook",getFacebook());
			XmlUtils.appendLeaf(xml,person,"linkedin",getLinkedin());
			XmlUtils.appendLeaf(xml,person,"state",getState());
			XmlUtils.appendLeaf(xml,person,"bio",getBio());
			
			Element skills = xml.createElement("skills");
			for(String skill : getSkills())
				XmlUtils.appendLeaf(xml,skills,"skill",skill);
			person.appendChild(skills);
			
			Element interests = xml.createElement("interests");
			for(String interest: getInterests())
				XmlUtils.appendLeaf(xml,interests,"interest",interest);
			person.appendChild(interests);
		xml.appendChild(person);
		return xml;
	}
	
	
	public void setFieldFromRawValue(String fieldname, String fieldvalue) {
		setFieldFromRawValue(fieldname,fieldvalue,null,null);
	}
	
	public void setFieldFromRawValue(String fieldname, String fieldvalue, SolrSession session) {
		setFieldFromRawValue(fieldname,fieldvalue,null,session);
	}
	
	public void setFieldFromRawValue(String fieldname, String fieldvalue, String permissions) {
		setFieldFromRawValue(fieldname,fieldvalue,permissions,null);
	}
	
	public void setFieldFromRawValue(String fieldname, Object fieldvalue,String permissions, SolrSession session) {
		//Switch in the fieldname for speed
		if(fieldname.equals("bio"))
			setBio((String)fieldvalue);
		else if(fieldname.equals("bookmarks"))
			setBookmarks(SerialUtils.loadBookmarks((String)fieldvalue,this,session));
		else if(fieldname.equals("department"))
			setDepartment((String)fieldvalue);
		else if(fieldname.equals("email"))
			setEmail((String)fieldvalue);
		else if(fieldname.equals("email2"))
			setEmail2((String)fieldvalue);
		else if(fieldname.equals("facebook"))
			setFacebook((String)fieldvalue);
		else if(fieldname.equals("firstName"))
			setFirstName((String)fieldvalue);
		else if(fieldname.equals("fullName"))
			setFullName((String)fieldvalue);
		else if(fieldname.equals("interests"))
			setInterests(SerialUtils.loadStringSet((String)fieldvalue));
		else if(fieldname.equals("irc"))
			setIrc((String)fieldvalue);
		else if(fieldname.equals("lastName"))
			setLastName((String)fieldvalue);
		else if(fieldname.equals("linkedin"))
			setLinkedin((String)fieldvalue);
		else if(fieldname.equals("location"))
			setLocation((String)fieldvalue);
		else if(fieldname.equals("permissions"))
			setPermissions(SerialUtils.loadSetHash(permissions));
		else if(fieldname.equals("phone"))
			setPhone((String)fieldvalue);
		else if(fieldname.equals("phone2"))
			setPhone2((String)fieldvalue);
		else if(fieldname.equals("picture"))
			setPicture((String)fieldvalue);
		else if(fieldname.equals("skills"))
			setSkills(SerialUtils.loadStringSet((String)fieldvalue));
		else if(fieldname.equals("state"))
			setState((String)fieldvalue);
		else if(fieldname.equals("title"))
			setTitle((String)fieldvalue);
		else if(fieldname.equals("twitter"))
			setTwitter((String)fieldvalue);
		else if(fieldname.equals("uid"))
			setUid((String)fieldvalue);
		else if(fieldname.equals("user_credential"))
			setCredentials(SerialUtils.loadStringSet((String)fieldvalue));
	}
}
