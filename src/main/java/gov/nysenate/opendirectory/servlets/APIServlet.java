package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;
import gov.nysenate.opendirectory.utils.XmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class APIServlet extends BaseServlet {
	//TODO Documentation must be produced
	
	Transformer xmlTransformer;
	
	public APIServlet() throws ServletException {
		super();
		
		//Load the Transformer resource. If this fails the servlet should die.
		try {
			xmlTransformer = TransformerFactory.newInstance().newTransformer();
			xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new ServletException("xmlTransformer Configuration Error",e);
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			throw new ServletException("TransformerFactory Configuration Error",e);
		}

	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Set up the request
		Request self = new Request(this,request,response);
		ServletOutputStream out = response.getOutputStream();
		
		//Branch on Version #
		String version = urls.getCommand(request);
	    if (version != null) {
	    	
	    	if(version.equals("1.0")) {
				new Version1_0(self,out).execute(urls.getArgs(request));
	    		
    		//Improper Version Argument
	    	} else {
	    		out.println("Version must be 1.0 for now");
	    	}
	    	
		//Display API documentation
	    } else {
	    	out.println("Only valid command right now is:");
	    	out.println("\t api/person/uid/<uid>.<format>");
	    }
	}
	
	public class Version1_0 {
		
		public class ApiException extends Exception {
			public ApiException(String msg) { super(msg); }
			public ApiException(String msg, Throwable t) { super(msg,t); }
		};
		
		protected Request self;
		protected ServletOutputStream out;
		protected TreeSet<String> methodSet;
		protected TreeSet<String> formatSet;
		
		public Version1_0(Request self,ServletOutputStream out) {
			this.out = out;
			this.self = self;
			
			String methods = "location,department,firstName,lastName,fullName,skills,interests,title,bio";
			methodSet = new TreeSet<String>(Arrays.asList(methods.split(",")));
			formatSet = new TreeSet<String>(Arrays.asList("xml".split(",")));
		}
		
		
		public void execute(Vector<String> args) {
			try {
				//Branch on Command
				if(!args.isEmpty()) {
		    		String command = args.remove(0);
		    		
		    		if(command.equals("person")) {
		    			doPerson(args);
		    			
		    		} else if(command.equals("search")) { 
		    			doSearch(args);
		    			
					} else {
		    			//Handle other cases
						throw new ApiException("bad command ("+command+") available commands are `person` and `search`");
		    		}
		    	} else {
		    		//Do some sort of return about the options, instructions or w/e
		    		throw new ApiException("no command given. available commands are `person` and `search`");
		    	}
			} catch (ApiException e) {
				//Handle the ApiException
				writeException(e);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void doSearch(Vector<String> args) throws ApiException, IOException {
			if(!args.isEmpty()) {
				
				String format = args.get(0);
				if(formatSet.contains(format)) {
					String query = self.httpRequest.getParameter("query");
					ArrayList<Person> people = self.solrSession.loadPeopleByQuery(query);
					
					if(people.isEmpty()) {
						Pattern pattern = Pattern.compile("(\\w+?):(\\w+?)(\\s(AND|OR)|$)");
						Matcher matcher = pattern.matcher(query);
						if(!matcher.find()) {
							people = self.solrSession.loadPeopleByQuery(query + "*");
							if(people.isEmpty()) {
								people = self.solrSession.loadPeopleByQuery(query + "~");
							}
						}
						else {
							people = self.solrSession.loadPeopleByQuery(matcher.replaceAll("$1:$2*$3"));
							
							if(people.isEmpty()) {
								people = self.solrSession.loadPeopleByQuery(matcher.replaceAll("$1:$2~$3"));
							}
						}
					}
					
					writeResponse(people,format);
					
				} else {
					throw new ApiException("Format not supported");
				}
				
			} else {
				throw new ApiException("search requires a format");
			}
		}
		
		public void doPerson(Vector<String> args) throws ApiException, IOException {
			
			//Branch on filter method    			
			if(!args.isEmpty()) {
				String method = args.remove(0);
				
				if(!args.isEmpty()) {
					
    				if(method.equals("uid")) {
    					
    					String parts[] = splitArg(args.remove(0));
    					if(parts != null) {
    						
							String term = parts[0];
	    					String format = parts[1];
	    					
	    					if(formatSet.contains(format)) {
	    						writeResponse(new ArrayList<Person>(Arrays.asList(self.solrSession.loadPersonByUid(term))),format);
	    					}
	    					
    					} else {
    						throw new ApiException("Format must be specified as <uid>.<format>");
    					}
						
    				} else if (methodSet.contains(method)) {

    					String parts[] = splitArg(args.remove(0));
    					if(parts != null) {
    						
							String term = parts[0];
	    					String format = parts[1];
	    					
	    					if(formatSet.contains(format)) {
	    						writeResponse(self.solrSession.loadPeopleByQuery(method+":("+term+")"),format);
	    					} else {
	    						throw new ApiException("Format "+format+"is currently not recognized!");
	    					}

    					} else {
    						throw new ApiException("Format must be specified as <term>.<format>");
    					}
	    					
    				} else {
    					throw new ApiException("Method not yet supported");
    				}
    				
				//All methods require exactly one argument
				} else {
					throw new ApiException("person/"+method+" requires an argument to be supplied.");
				}
				
			} else {
				//They need to specify a method
				throw new ApiException("You must specify a method");
			}
		}
	
		
		public void writeException(ApiException exception) {
			
			Document xml;
			try {
				xml = XmlUtils.newDocument();
				Element response = xml.createElement("response");
					Element metadata = xml.createElement("metadata");
						XmlUtils.appendLeaf(xml,metadata,"status","success");
						XmlUtils.appendLeaf(xml,metadata,"message",exception.getMessage());
						XmlUtils.appendLeaf(xml,metadata,"total","");
					response.appendChild(metadata);
					Element data = xml.createElement("data");
					response.appendChild(data);
				xml.appendChild(response);
				xmlTransformer.transform(new DOMSource(xml), new StreamResult(out));
				
			} catch (TransformerException e) {
				// TODO handle writeException Xml TransformationException
				e.printStackTrace();
			}
			
		}
		
		public void writeException(ApiException e, String format) {
			
		}
		
		public void writeResponse(ArrayList<Person> results, String format) {
			
			try {
				Document xml = XmlUtils.newDocument();
				Element response = xml.createElement("response");
					Element metadata = xml.createElement("metadata");
						XmlUtils.appendLeaf(xml,metadata,"status","success");
						XmlUtils.appendLeaf(xml,metadata,"message","");
						XmlUtils.appendLeaf(xml,metadata,"total",String.valueOf(results.size()));
					response.appendChild(metadata);
					Element data = xml.createElement("data");
						for(Person p : results) {
							data.appendChild(xml.importNode(p.toXml().getDocumentElement(), true));
						}
					response.appendChild(data);
				xml.appendChild(response);
				xmlTransformer.transform(new DOMSource(xml), new StreamResult(out));
				
			} catch (TransformerException e) {
				// TODO Handle case where transformer fails, bad data perhaps?
				e.printStackTrace();
			}
		}
		
	}
	
	public String[] splitArg(String arg) {
		String[] parts = arg.split("\\.");
		if(parts.length==2)
			return parts;
		return null;
	}
}