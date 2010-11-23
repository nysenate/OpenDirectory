package gov.nysenate.opendirectory.solr;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.TreeSet;
import java.util.HashMap;

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
	        
	        //This is the TreeSet that will house credentials for each field
	        TreeSet<String> field_credentials = new TreeSet<String>();
	        
	        Boolean approved;
	        
	        for(int c=0; c<fields.getLength(); c++)
	        {
	        	approved=false;
	        	field_credentials = Credentials((String)fields.item(c).getAttributes().item(0).getNodeValue());
	        	
        		if(user.equals(Person.getAdmin()))
        			approved = true;
        		else {
	        		for(String temp : field_credentials) {
	        			if(user.getCredentials().contains(temp) == true) {
		        			approved = true;
		        			break;
			        	} 
	        		}
        		}
        		
        		String fieldname = (String)fields.item(c).getAttributes().item(1).getNodeValue();
        		
        		if(fieldname.equals("user_credential") || fieldname.equals("skills") || fieldname.equals("interests")) {
        			if(approved)
        				person.setCredentials(Credentials((String)profile.getFieldValue(fieldname)));
        			else
        				person.setCredentials(null);
        		} else if(fieldname.equals("permissions")){
        			if(!approved)
        				person.setPermissions(null);
        			else{
        				HashMap<String, TreeSet<String>> permission = new HashMap<String, TreeSet<String>>();
        		        
        				for(int i=0; i<fields.getLength(); i++)
        				{
        					field_credentials= Credentials((String)fields.item(i).getAttributes().item(0).getNodeValue());
        					fieldname = (String)fields.item(i).getAttributes().item(1).getNodeValue();
        					permission.put(fieldname, field_credentials);
        				}
        				
        				person.setPermissions(permission);
        			}
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
	
	public TreeSet<String> Credentials(String credentials){
		
		TreeSet<String> Credentials = new TreeSet<String>();
		
		String[] credentialset = credentials.split(", ");
		
		for(String temp : credentialset)
		{
			Credentials.add(temp);
		}
		
		return Credentials;
	}
	
}
