package gov.nysenate.opendirectory.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServiceFilter implements Filter {
	
	private final String IP_MATCH = "(10.\\d+.\\d+.\\d+|127.0.0.1|63.118.5[67].\\d+)";
	
    public ServiceFilter() {
        
    }
    
	public void destroy() {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if(((HttpServletRequest)request).getSession().getAttribute("uid") == null) {
			if(!request.getRemoteAddr().matches(IP_MATCH)) {
				String uri = ((HttpServletRequest)request).getRequestURI();
				
				if(uri.contains("/external") || uri.matches("^/opendirectory/(css|img)/.+$") || uri.startsWith("http://www.nysenate.gov")) {
					chain.doFilter(request, response);
				}
				else {
					((HttpServletResponse)response).sendRedirect("http://www.nysenate.gov");
				}
			}
			else {
				chain.doFilter(request, response);
			}
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
