package gov.nysenate.opendirectory.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtils {

	private static DocumentBuilder builder;
	
	public static DocumentBuilder getBuilder() {
		if(builder==null)
			try {
				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO come up with some sort of recovery process
				e.printStackTrace();
			}
		return builder;
	}
	
	public static Document newDocument() {
		return getBuilder().newDocument();
	}
	
	public static Element createLeaf(Document doc, String name, String value) {
		Element leaf = doc.createElement(name);
		if(value!=null)
			leaf.appendChild(doc.createTextNode(value));
		return leaf;
	}
	
	public static void appendLeaf(Document doc, Element root, String name, String value) {
		root.appendChild(createLeaf(doc,name,value));
	}
	
	/*
	//Returns permissions for each field in "xml" string
	public String Permissions(HashMap<String,TreeSet<String>> permissions)
	{
		Iterator<?> permission = permissions.keySet().iterator();
		
		//XML to be written
		String credentials = new String();
		credentials="<fields>";
		
		String field;
		String credential_list;
		String access_level;
		
		while(permission.hasNext())
		{
			field = permission.next().toString();
			credential_list = permissions.get(field).toString();
			access_level = credential_list.substring(1, credential_list.length()- 1);
			
			credentials+="<field name=\"" + field + "\" allow = \"" + 
				access_level + "\"/>"; 
		}
		credentials+="</fields>";
		return credentials;
	}
	
	public String Bookmarks(HashMap<String, TreeSet<String>> BOOKMARK)
	{
		Iterator<?> bookmark_iterator = BOOKMARK.keySet().iterator();
		
		//XML to be written
		String bookmarks= new String();
		bookmarks="<users>";
		
		String id;
		String employee_list;
		String fullname;
		
		while(bookmark_iterator.hasNext()) {
			id = bookmark_iterator.next().toString();
			employee_list = BOOKMARK.get(id).toString();
			fullname = employee_list.substring(1, employee_list.length()-1);
			
			bookmarks+="<user id=\"" + id + "\" fullName = \"" + fullname + "\"/>";
		}
		
		bookmarks+="</users>";
		return bookmarks;
	}
	 */
}
