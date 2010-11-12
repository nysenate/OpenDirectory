package gov.nysenate.opendirectory.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.solr.client.solrj.beans.Field;

public class Person {
	/** 
	 * Designed according to JavaBean specs for use in the
	 * Java Expressions Language (EL) of JSP 2.0+
	**/
	
	public Person() {}
	
	private TreeSet<String> credentials;
	private HashMap<String,TreeSet<String>> permissions;
	
	@Field
	private String firstName;
	@Field
	private String lastName;
	@Field("Title")
	private String title;
	@Field("id")
	private String uid;
	@Field
	private String fullName;
	@Field
	private String state;
	@Field
	private String location;
	@Field
	private String department;
	@Field
	private String phone;
	@Field
	private String email;
	
		
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
		}
		return admin;
	}
	
	public static Person getAnon() {
		if(anon == null) {
			anon = new Person();
			anon.setFullName("Anonymous User");
		}
		return anon;
	}
}
