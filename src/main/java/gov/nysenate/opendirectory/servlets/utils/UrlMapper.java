package gov.nysenate.opendirectory.servlets.utils;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class UrlMapper {

	public final String context = "opendirectory";
	
	public UrlMapper() {}
	
	public String url(String controller) {
		return url(controller,null);
	}
	
	public static void main(String[] args) {
		UrlMapper urls = new UrlMapper();
		System.out.println(urls.url("person","opendirectory"));
		System.out.println(urls.getCommand("/opendirectory/solr/"));
		System.out.println(urls.url("css","style.css"));
	}
	
	public String url(String controller,String command,String...args) {
		String url = "/"+context+"/";
		
		if(controller.equals("index"))
			return url+"home";
		else if(controller.startsWith("img"))
			url += controller;
		else
			url += controller+'/';
		
		if(command!=null)
			url += command;
		
		for (String arg : args) {
			url+='/'+arg;
		}
		return url;
	}
	
	public String getCommand(HttpServletRequest request) {
		return getCommand(request.getRequestURI());
	}

	public Vector<String> getArgs(HttpServletRequest request) {
		return getArgs(request.getRequestURI());
	}
	
	public String getCommand(String url) {
		if(url.startsWith("/"))
			url = url.substring(1);
		
		String[] tokens = url.split("/");
		if(tokens.length < 3) {
			return null;
		} else {
			return tokens[2];
		}
	}
	
	public Vector<String> getArgs(String url) {
		if(url.startsWith("/"))
			url = url.substring(1);
		
		String[] tokens = url.split("/");
		Vector<String> args = new Vector<String>();
		if(tokens.length > 3) {
			for(int i=3; i < tokens.length; i++) {
				args.add(tokens[i]);
			}
		}
		return args;
	}
	
	@SuppressWarnings("serial")
	public class UrlException extends Exception {
		public UrlException() { super(); }
		public UrlException(String msg) { super(msg); }
	};
}
