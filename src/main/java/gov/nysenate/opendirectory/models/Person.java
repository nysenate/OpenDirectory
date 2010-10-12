package gov.nysenate.opendirectory.models;

import javax.naming.NamingException;
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

	public Person() {}

	public Person(SearchResult record) throws NamingException {
		Attributes attributes = record.getAttributes();
		this.email = (String)attributes.get("mail").get();
		this.phone = (String)attributes.get("telephonenumber").get();
		this.state = (String)attributes.get("st").get();
		this.department = (String)attributes.get("department").get();
		this.title = (String)attributes.get("title").get();
		this.firstName = (String)attributes.get("givenname").get();
		this.lastName = (String)attributes.get("sn").get();
		this.fullName = (String)attributes.get("displayname").get();
		this.uid = (String)attributes.get("uid").get();
		this.location = (String)attributes.get("location").get();
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

}
