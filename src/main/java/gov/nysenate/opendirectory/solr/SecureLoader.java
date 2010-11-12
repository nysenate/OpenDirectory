package gov.nysenate.opendirectory.solr;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.TreeSet;

import org.apache.solr.common.SolrDocument;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;

import gov.nysenate.opendirectory.models.Person;

public class SecureLoader {

	private Person user;
	
	public SecureLoader(Person user) {
		this.user = user;
	}
	
	public Person loadPerson(SolrDocument profile) {
		//Do the loading here
		Person person = new Person();
		
		//Dependent on matching user credentials and field credentials
		//load into the person object		
		String permissions_xml = (String)profile.getFieldValue("permissions");

		try {
	        DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(permissions_xml));
	        
	        Document doc = db.parse(is);
	       
	        NodeList fields = doc.getDocumentElement().getChildNodes();
	        //Node field = new Node();
	        TreeSet<String> permissions = new TreeSet<String>();
	        
	        Boolean approved;
	        
	        for(int c=0; c<fields.getLength(); c++)
	        {
	        	approved=false;
	        	permissions = Permission((String)fields.item(c).getAttributes().item(0).getNodeValue());
	        	
        		if(user.equals(Person.getAdmin()))
        			approved = true;
        		else {
	        		for(String temp : permissions) {
	        			if(user.getCredentials().contains(temp) == true) {
		        			approved = true;
		        			break;
			        	} 
	        		}
        		}
        		String fieldname = (String)fields.item(c).getAttributes().item(1).getNodeValue();
        		if(fieldname.equals("user_credential")) {
        			if(approved)
        				person.setCredentials(Permission((String)profile.getFieldValue("user_credential")));
        			else
        				person.setCredentials(null);
        			
        		} else {
        			String setFieldName = "set"+fieldname.substring(0, 1).toUpperCase()+fieldname.substring(1);
            		Method setMethod = person.getClass().getMethod(setFieldName, String.class);
        			if(approved)
        				setMethod.invoke(person, profile.getFieldValue(fieldname));
        			else
        				setMethod.invoke(person, (String)null);
        		}
	        }
		}
	    
		catch (Exception e) {
	        e.printStackTrace();
	    }
		return person;
	}
	
	public TreeSet<String> Permission(String credentials){
		
		TreeSet<String> permissions = new TreeSet<String>();
		
		String[] credentialset = credentials.split(", ");
		
		for(String temp : credentialset)
		{
			permissions.add(temp);
		}
		
		return permissions;
	}
	
}
