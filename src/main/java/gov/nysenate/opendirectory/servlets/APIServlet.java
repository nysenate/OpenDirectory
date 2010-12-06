package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.utils.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
					writeResponse(self.solrSession.loadPeopleByQuery(self.httpRequest.getParameter("query")),format);
					
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
	    						writeResponse(self.solrSession.loadPeopleByQuery(method+":(*"+term+"*)"),format);
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
				xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element response = xml.createElement("response");
					Element metadata = xml.createElement("metadata");
						appendLeaf(xml,metadata,"status","success");
						appendLeaf(xml,metadata,"message",exception.getMessage());
						appendLeaf(xml,metadata,"total","");
					response.appendChild(metadata);
					Element data = xml.createElement("data");
					response.appendChild(data);
				xml.appendChild(response);
				
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				transformer.transform(new DOMSource(xml), new StreamResult(out));
				
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		public void writeException(ApiException e, String format) {
			
		}

		public Element createLeaf(Document doc, String name, String value) {
			Element leaf = doc.createElement(name);
			if(value!=null)
				leaf.appendChild(doc.createTextNode(value));
			return leaf;
		}
		
		public void appendLeaf(Document doc, Element root, String name, String value) {
			root.appendChild(createLeaf(doc,name,value));
		}
		
		public void writeResponse(ArrayList<Person> results, String format) {
			try {
				Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element response = xml.createElement("response");
					Element metadata = xml.createElement("metadata");
						appendLeaf(xml,metadata,"status","success");
						appendLeaf(xml,metadata,"message","");
						appendLeaf(xml,metadata,"total",String.valueOf(results.size()));
					response.appendChild(metadata);
					Element data = xml.createElement("data");
						for(Person p : results) {
							data.appendChild(xml.importNode(p.toXml().getDocumentElement(), true));
						}
					response.appendChild(data);
				xml.appendChild(response);
				
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				transformer.transform(new DOMSource(xml), new StreamResult(out));
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
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