package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;
import gov.nysenate.opendirectory.utils.XmlUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.utils.Base64;


@SuppressWarnings("serial")
public class PersonServlet extends BaseServlet {
	
	private String avatarPath;
	
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
	

	public class PersonServletException extends Exception {
		public PersonServletException(String m) { super(m); }
		public PersonServletException(String m, Throwable t) { super(m,t); }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request self = new Request(this,request,response);
	    String uid = urls.getCommand(request);
	    
	    try {
		    if(uid == null)
		    	throw new PersonServletException("No uid was supplied");
		    
		    Person person = self.solrSession.loadPersonByUid(uid);
		    if(person==null)
		    	throw new PersonServletException("Bad uid was supplied");
		    
	    	Vector<String> args = urls.getArgs(request);
	    	if(args.size()==0)
	    		self.redirect(urls.url("person",uid,"profile"));
	    		//args.add("profile"); //throw new PersonServletException("No command was supplied");
	    	else {
	    		String command = args.get(0);
		    	if(command.equals("profile")) {
			    	request.setAttribute("person", person);
			    	request.setAttribute("title", person.getFirstName()+" "+person.getLastName()+" | NYSS Open Directory");
			    	self.render("profile.jsp");
			    	
		    	} else if (command.equals("vcard")) {
		    		response.setContentType("text/x-vcard;charset=UTF-8");
		    		ServletOutputStream out = response.getOutputStream();
		    		StringBuilder mResult = new StringBuilder();
					mResult.append("BEGIN:VCARD\r\n");
					mResult.append("VERSION:3.0\r\n");
					mResult.append("\r\nN:").append(person.getLastName()).append(";").append(person.getFirstName()).append(";;;");
					mResult.append("\r\nFN:").append(person.getFullName());
					mResult.append("\r\nORG:").append("New York State Senate").append(";").append(person.getDepartment());
					mResult.append("\r\nTITLE:").append(person.getTitle());
					mResult.append("\r\nTEL;TYPE=WORK;TYPE=PREF:").append(person.getPhone());
					mResult.append("\r\nTEL;TYPE=WORK:").append(person.getPhone2());
					mResult.append("\r\nEMAIL;TYPE=WORK;TYPE=PREF;TYPE=INTERNET:").append(person.getEmail());
					mResult.append("\r\nEMAIL;TYPE=WORK;TYPE=INTERNET:").append(person.getEmail2());
					mResult.append("\r\nitem2.URL:").append(person.getTwitter());
					mResult.append("\r\nitem2.X-ABLabel:").append("Twitter");
					mResult.append("\r\nitem3.URL;LINKEDIN:").append(person.getLinkedin());
					mResult.append("\r\nitem3.X-ABLabel:").append("LinkedIn");
					mResult.append("\r\nitem4.URL;FACEBOOK:").append(person.getFacebook());
					mResult.append("\r\nitem4.X-ABLabel:").append("Facebook");
					if(person.getPicture() != null && !person.getPicture().equals("")) {
						BufferedImage image = ImageIO.read(new File(avatarPath() + "thumb/" + person.getPicture()));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(image, "png",baos);
						baos.flush();
						String encodedImage = Base64.encode(baos.toByteArray());
						baos.close();
						mResult.append("\r\nPHOTO;TYPE=JPEG;ENCODING=BASE64:" + encodedImage.replaceAll("[\n\r]", ""));
					}
					mResult.append("\r\nEND:VCARD\r\n");
					
					out.print(mResult.toString());
					
		    	} else
		    		throw new PersonServletException("Invalid command `"+command+"` was supplied.");
	    	}
	    	
	    } catch (PersonServletException e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	    	if(e.getCause()!=null)
	    		e.getCause().printStackTrace();
	    	self.redirect(urls.url("index"));
	    }
	    
	}
}