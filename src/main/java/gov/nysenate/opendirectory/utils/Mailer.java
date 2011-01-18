package gov.nysenate.opendirectory.utils;

import gov.nysenate.opendirectory.models.ExternalPerson;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {
	private static final String SMTP_HOST_NAME = Resource.get("hostname");

	private static final String SMTP_PORT = Resource.get("port");
	
	private static final String SMTP_ACCOUNT_USER = Resource.get("user");
	private static final String SMTP_ACCOUNT_PASS = Resource.get("pass");
	
	public static void sendMail(String to, String subject, String message, String from, String fromDisplay) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.starttls.enable","false");
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.ssl.enable","false");

		Session session = Session.getDefaultInstance(props,	new javax.mail.Authenticator() {
										protected PasswordAuthentication getPasswordAuthentication() {
											return new PasswordAuthentication(SMTP_ACCOUNT_USER, SMTP_ACCOUNT_PASS);}});
		session.setDebug(false);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		addressFrom.setPersonal(fromDisplay);
		msg.setFrom(addressFrom);
	
		
		StringTokenizer st = new StringTokenizer (to,",");
		
		InternetAddress[] rcps = new InternetAddress[st.countTokens()];
		int idx = 0;
		
		while (st.hasMoreTokens())
		{
			InternetAddress addressTo = new InternetAddress(st.nextToken());
			rcps[idx++] = addressTo;
			
		}
		
		msg.setRecipients(Message.RecipientType.TO,rcps);
		
		msg.setSubject(subject);
		msg.setContent(message, "text/html");
		Transport.send(msg);
	}

	public static void sendExternalAuthorizationMail(ExternalPerson person) {
		String authUrl = "http://directory.nysenate.gov/external/auth?email=" + person.getEmail() +
		"&key=" + person.getAuthorizationHash();
		
		String to = person.getEmail();
		String subject = "Authorize your NYSS OpenDirectory account";
		String message = "Hello " + person.getFirstName() + ", <br/><br/>" + 
			"It appears that you signed up to view the NYSS OpenDirectory, " +
			"in order to finalize this subscription you must click <a href\"" + authUrl + "\">here<a/> or open the following url: <br/><br/>" +
			authUrl + "<br/><br/>" + 
			"If you have any questions please <a href=\"http://www.nysenate.gov/contact\">contact us</a>.<br/><br/>";
		
		String from = "OpenDirectory@nysenate.gov";
		String fromDisplay = "NYSS OpenDirectory";
		
		try {
			sendMail(to, subject, message, from, fromDisplay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}