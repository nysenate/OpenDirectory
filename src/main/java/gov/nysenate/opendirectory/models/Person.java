package gov.nysenate.opendirectory.models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class Person {
	/** 
	 * Designed according to JavaBean specs for use in the
	 * Java Expressions Language (EL) of JSP 2.0+
	**/
	
	public Person() {
		
		//Need to set defaults for everything else here...
		setBio(null);
		setEmail2(null);
		setPhone2(null);
		setTwitter(null);
		setFacebook(null);
		setLinkedin(null);
		setIrc(null);
		setSkills(null);
		setInterests(null);
		setBookmarks(null);
		setPicture(null);
		
		//All people must have permissions and credentials
		setPermissions(Person.getDefaultPermissions());
		setCredentials(new TreeSet<String>(Arrays.asList("public")));
	}
	
	
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
	private HashMap<String, String> bookmarks;
	
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
	public HashMap<String, String> getBookmarks() {
		return bookmarks;
	}
	
	public void setBookmarks(HashMap<String, String> bookmarks) {
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
						}
					}
				}
			}
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
						}
					}
				}
			}
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
						}
					}
				}
			}
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
						}
					}
				}
			}
			return ret;
		}
	}
	
	static Person admin;
	static Person anon;
	
	public static Person getAdmin() {
		if(admin == null) {
			admin = new Person();
			admin.setFullName("Administrator");
			
			TreeSet<String> cred_admin = new TreeSet<String>();
			cred_admin.add("public");
			cred_admin.add("admin");
			admin.setPermissions(new HashMap<String,TreeSet<String>>());
			admin.setCredentials(cred_admin);	
		}
		return admin;
	}
	
	public static Person getAnon() {
		if(anon == null) {
			anon = new Person();
			anon.setFullName("Anonymous User");
			
			TreeSet<String> cred_default = new TreeSet<String>();
			cred_default.add("public");
			anon.setPermissions(new HashMap<String, TreeSet<String>>());
			anon.setCredentials(cred_default);
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
	
	
}
