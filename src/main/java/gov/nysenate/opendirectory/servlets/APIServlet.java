package gov.nysenate.opendirectory.servlets;

import gov.nysenate.opendirectory.models.Person;
import gov.nysenate.opendirectory.servlets.utils.BaseServlet;
import gov.nysenate.opendirectory.servlets.utils.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		
		public void execute(Vector<String> args) throws IOException {
			//Branch on Command
			if(!args.isEmpty()) {
	    		String command = args.remove(0);
	    		
	    		if(command.equals("person")) {
	    			doPerson(args);
	    			
	    		} else if(command.equals("search")) { 
	    			doSearch(args);
	    			
				}else {
	    			//Handle other cases
					out.println("bad command ("+command+") available commands are `person` and `search`");
	    		}
	    	} else {
	    		//Do some sort of return about the options, instructions or w/e
	    		out.println("no command given. available commands are `person` and `search`");
	    	}
		}
		
		public void doSearch(Vector<String> args) throws IOException {
			if(!args.isEmpty()) {
				
				String format = args.get(0);
				if(formatSet.contains(format)) {
					String query = self.httpRequest.getParameter("query");
					ArrayList<Person> people = self.solrSession.loadPeopleByQuery(query);
					if(format.equals("xml")) {
						for(Person person : people) {
							out.println(person.toString());
						}
					}
				} else {
					out.println("Format not supported");
				}
				
			} else {
				out.println("search requires a format");
			}
		}
		
		public void doPerson(Vector<String> args) throws IOException {
			
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
			    					Person person = self.solrSession.loadPersonByUid(term);
			    					if(format.equals("xml")) {
			    						out.println(person.toString());
			    					} 
		    					}
		    					
	    					} else {
	    						out.println("Format currently unrecognized");
	    					}
    						
	    				} else if (methodSet.contains(method)) {

	    					String parts[] = splitArg(args.remove(0));
	    					if(parts != null) {
	    						
								String term = parts[0];
		    					String format = parts[1];
		    					
		    					if(formatSet.contains(format)) {
		    						String query = method+":(*"+term+"*)";
		    						System.out.println("Query was: "+query);
		    						ArrayList<Person> people = self.solrSession.loadPeopleByQuery(query);
			    					if(format.equals("xml")) {
			    						for(Person person : people) {
			    							out.println(person.toString());
			    						}
									}
		    					}
		    					
	    					else {
	    						out.println("Format currently unrecognized");
	    					}
	    					
	    				} else {
	    					out.println("Method not yet supported");
	    				}
	    				
    				//The term could not be properly split into parts
					} else {
						out.println("Bad format for the <id>.<format>");
					}
    				
				//All methods require exactly one argument
				} else {
					out.println("person/"+method+" requires an argument to be supplied.");
				}
			} else {
				//They need to specify a method
				out.println("You must specify a method");
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