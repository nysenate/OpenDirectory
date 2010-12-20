package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.ldap.Ldap;
import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;
import gov.nysenate.opendirectory.utils.XmlUtils;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.solr.client.solrj.SolrServerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


@SuppressWarnings("serial")
public class UserServlet extends BaseServlet {
	
	private String avatarPath;
	
	public class UserServletException extends Exception {
		public UserServletException(String m) { super(m); }
		public UserServletException(String m, Throwable t) { super(m,t); }
	}
	
	public UserServlet() { super(); }
	
	public String avatarPath() {
		if(avatarPath==null) {
			try {
				String s = System.getProperty("file.separator");
				File configFile = new File(getServletContext().getRealPath(s)+s+"WEB-INF"+s+"config.xml");
				Document config = XmlUtils.getBuilder().parse(configFile);
				Element avatar = (Element)config.getDocumentElement().getElementsByTagName("avatars").item(0);
				String path = (String)avatar.getAttributes().getNamedItem("value").getTextContent().trim();
				return path;
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return avatarPath;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
		String command = urls.getCommand(request);
		
    	try {
    		
    		//Redirect to login page for bad access
    	    if(command == null)
    	    	throw new UserServletException("No command supplied to GET request.");
    	    
    		//Send them to the login page or redirect them.
	    	if(command.equals("login")) {
	    		if( self.httpSession.getAttribute("uid") == null)
	    			self.render("login.jsp");
	    		else	    			
	    			self.redirect(urls.url("person",(String)self.httpSession.getAttribute("uid")));
	    	
	    	//Remove the attribute and redirect (its fine if the attribute isn't there)
	    	} else if (command.equals("logout")) {
    			self.httpSession.removeAttribute("uid");
	    		self.redirect(urls.url("index"));
	    	
	    	//Since its a GET request, just render the edit page
	    	} else if (command.equals("edit")) {
	    		if(self.user.equals(Person.getAnon()))
	    			self.redirect(urls.url("user","login"));
	    		else
	    			self.render("EditProfile.jsp");
	    		
	    	} else if (command.equals("bookmarks")) {
	    		if(self.user.equals(Person.getAnon()))
	    			self.redirect(urls.url("user","login"));
	    		else
	    			doBookmarks(self);
	    		
	    	} else
	    		throw new UserServletException("Invalid command `"+command+"` supplied.");
	    	
	    //Print the errors to Sys.out and redirect to login
    	} catch (UserServletException e) {
    		doException(self,e);
    	}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String command = urls.getCommand(request);
	    
	    try {
		    //If they are posting to a bad place, 
		    if(command == null)
		    	throw new UserServletException("No command supplied to POST request.");
		    
	    	if(command.equals("login")) {
	    		doLogin(self);
	    		
	    	} else if (command.equals("edit")) {
	    		if(self.user.equals(Person.getAnon()))
	    			self.redirect(urls.url("user","login"));
	    		else
	    			doEdit(self);
	    		
	    	} else
	    		throw new UserServletException("Invalid command `"+command+"` supplied.");
	    	
	    } catch (UserServletException e) {
	    	doException(self,e);
	    }
	}
	
	public void doException(Request self, UserServletException e) throws ServletException, IOException {
    	System.out.println(e.getMessage());
    	e.printStackTrace();
    	if(e.getCause()!=null)
    		e.getCause().printStackTrace();
    	self.redirect(urls.url("user","login"));
	}
	
	public void doLogin(Request self) throws UserServletException, ServletException, IOException {
		String cred = (String)self.httpRequest.getParameter("name");
		String pass = (String)self.httpRequest.getParameter("password");
		
		//If they are already logged in, forward to their profile
		if(self.httpSession.getAttribute("uid") != null ) {
			self.redirect(urls.url("person",cred));
			
		// Check to see the user attempting to log in is valid before authenticating
		// This needs to be done because not all authenticating users are in the OpenDirectory
		} else if (self.solrSession.loadPersonByUid(cred) != null) {
    		try {
    			
    			//Attempt to authenticate and login the user with the credentials supplied
    			//Make temporary exceptions for non LDAP records (for testing)
    			if ( (cred.equalsIgnoreCase("opendirectory") && pass.equals("senbook2010"))
    					|| (cred.equalsIgnoreCase("chrib") && pass.equals("chrib1"))
    					|| (cred.equalsIgnoreCase("chrim") && pass.equals("chrim1"))
    					|| (cred.equalsIgnoreCase("graylin") && pass.equals("graylin1"))
    					|| Ldap.authenticate(cred,pass)) {
    				self.httpSession.setAttribute("uid",cred);
    				self.redirect(urls.url("person",cred,"profile"));
    				
    			//Otherwise, alert them that their combination was wrong and redirect them to try again
    			} else {
	    			self.httpRequest.setAttribute("errorMessage", "Username and/or password were incorrect.");
	    			self.render("/login.jsp");
    			}
    			
    		//There is a small chance that a connection to LDAP is not available for some reason
    		} catch (NamingException e) {
    			
    			//Record the Error to our logs
    			e.printStackTrace();
    			
    			//Alert the user to the situation
    			self.httpRequest.setAttribute("errorMessage", "The ldap server is down and you can't be authenticated. Please try again later.");
    			self.render("login.jsp");
    		}
		
		//Person is not in our database
		} else {
			self.httpRequest.setAttribute("errorMessage", "The user you have specified `"+cred+"` is not indexed in OpenDirectory.");
			self.render("login.jsp");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void doEdit(Request self) throws UserServletException, IOException, ServletException {
		self.httpRequest.setAttribute("message", "Changes Saved. <a href=\""+urls.url("person",self.user.getUid(),"profile")+"\">View your profile.</a>");

		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> files = upload.parseRequest(self.httpRequest);
			for(FileItem item : files) {
				
				//Handle normal fields
				if(item.isFormField()) {
					String key = item.getFieldName();
					String value = item.getString();
					
					//If its a radio button value, push it into the permissions map
					if(key.startsWith("radio_")) {
						self.user.getPermissions().put(key.substring(6), new TreeSet<String>(Arrays.asList(value.toLowerCase())));
						
					//Otherwise, transform the raw value and insert it into our user
					} else {
						self.user.loadField(key,value,self.solrSession);
					}
					
				//Handle file, but only if name is not empty
				//(so that we don't handle blank uploads)
				} else if(item.getName()!=null && item.getName().isEmpty()==false) {
					try {
						//Break down the filename to and build a new one with the user id
						String filetype = item.getName().substring(item.getName().lastIndexOf('.'));
						String filename = self.user.getUid()+filetype;
						
						//Write the file to the img/avatars directory and set the person's weblink
						System.out.println("Writing to: "+avatarPath()+filename);
						item.write(new File(avatarPath()+filename));
						self.user.setPicture("/uploads/avatars/"+filename);

					//Writing a FileItem can apparently through any kind of exception (sloppy)
					//so I don't know why this would get thrown here, just what throws it.
					} catch (Exception e) {
						throw new UserServletException("Failed to write uploaded file.",e);
					}
					
				//This means its an empty file item and we can safely ignore it
				} else { }
				
			}
			
			//Save the newly modified person, and re-render the edit page.
			self.solrSession.savePerson(self.user);
    		self.render("EditProfile.jsp");
			
		//Parsing a bad request can sometimes throw a FileUploadException
		// TODO I should figure out what to do here
		} catch (FileUploadException e) {
			throw new UserServletException("Failure to parse the uploaded file items from the request",e);
		} catch (SolrServerException e) {
			throw new UserServletException("Failure to save modified person state after edit", e);
		}
	}
	
	public void doBookmarks(Request self) throws UserServletException, IOException, ServletException {
		Vector<String> args = urls.getArgs(self.httpRequest);
		
		//With no action, view is implied
		if(args.isEmpty()) {
			self.render("bookmarks.jsp");
			
		} else if(args.size()==2) {
			
			//Try to load the user and add them to book marks before saving and redirecting
			if(args.get(0).equals("add")) {
				System.out.println("Adding user"+args.get(1));
	    		try {
		    		self.user.getBookmarks().add(self.solrSession.loadPersonByUid(args.get(1)));
					self.solrSession.savePerson(self.user);
					self.redirect(urls.url("person",args.get(1),"profile"));
				} catch (SolrServerException e) {
					throw new UserServletException("Solr Load/Save error on add bookmark.",e);
				}
				
			//Find and remove the specified user, speed shouldn't matter for small book mark lists
			//It doesn't matter if the person wasn't on the list in the first place, no harm done.
    		} else if(args.get(0).equals("remove")) {
    			try {
    				self.user.getBookmarks().remove(new Person(args.get(1)));
					self.solrSession.savePerson(self.user);
					self.redirect(urls.url("user","bookmarks"));
				} catch (SolrServerException e) {
					throw new UserServletException("Solr Load/Save error on remove bookmark.",e);
				}
    		} else
    			throw new UserServletException("Bad bookmarks command: "+args.get(0));
		} else
			throw new UserServletException("Improper number of arguments: "+args.size());
	}
}
