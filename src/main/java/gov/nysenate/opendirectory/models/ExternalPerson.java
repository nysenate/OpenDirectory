package gov.nysenate.opendirectory.models;

import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

import org.jasypt.util.text.BasicTextEncryptor;

import gov.nysenate.opendirectory.models.interfaces.IPerson;
import gov.nysenate.opendirectory.utils.BCrypt;


public class ExternalPerson implements IPerson {
	
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String hash;
	private TreeSet<String> credentials;
	public boolean authorized;
	public String authorizationHash;
	
	public ExternalPerson() {
		setFirstName("");
		setLastName("");
		setEmail("");
		setPhone("");
		setHash("");
		setAuthorized(false);
	}
	
	public ExternalPerson(String firstName, String lastName, String email,
			String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getHash() {
		return hash;
	}
	public TreeSet<String> getCredentials() {
		return new TreeSet<String>(Arrays.asList("senate"));
	}
	public boolean getAuthorized() {
		return authorized;
	}
	public String getAuthorizationHash() {
		return authorizationHash;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void setCredentials(TreeSet<String> credentials) {
		this.credentials = credentials;
	}
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	public void setAuthorizationHash(String authorizationHash) {
		this.authorizationHash = authorizationHash;
	}
	
	public void setAuthorizationHash() {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		String dateTime = Long.toString(new Date().getTime());
		textEncryptor.setPassword(dateTime + email);
		this.authorizationHash = textEncryptor.encrypt(
				dateTime + email).replaceAll("=|&|\\?|\\+|/|\\p{Cntrl}","");
	}
	public boolean checkPassword(String password) {
		return BCrypt.checkpw(password, hash);
	}
	
	public void encryptPassword(String password) {
		this.hash = BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	
}
