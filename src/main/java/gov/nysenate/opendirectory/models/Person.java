package gov.nysenate.opendirectory.models;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

/*
 * "displayname","location","givenname","uidnumber","uid", "mail", "cn",
 * "telephonenumber","st","l","sn","department", "title","gidnumber", "employeeid"
 */
public class Person {
	/* Designed according to JavaBean specs for use in the
	 * Java Expressions Language (EL) of JSP 2.0+
	 */
	private String getAttribute(Attributes attributes,String name) throws NamingException {
		Attribute attr = attributes.get(name);
		
		if(attr != null && attr.size() != 0)
			return (String)attr.get();
		else
			return null;
	}
	
	public Person() {}
	public Person(SearchResult record) throws NamingException {
		Attributes attributes = record.getAttributes();
		this.email = getAttribute(attributes,"mail");
		this.phone = getAttribute(attributes,"telephonenumber");
		this.state = getAttribute(attributes,"st");
		this.department = getAttribute(attributes,"department");
		this.title = getAttribute(attributes,"title");
		this.firstName = getAttribute(attributes,"givenname");
		this.lastName = getAttribute(attributes,"sn");
		this.fullName = getAttribute(attributes,"displayname");
		this.uid = getAttribute(attributes,"uid");
		this.location = getAttribute(attributes,"l");
	}
	
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
}
