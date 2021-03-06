package gov.nysenate.opendirectory.filters;

import gov.nysenate.opendirectory.utils.UrlMapper;

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
	
//	private final String IP_MATCH = "(10.\\d+.\\d+.\\d+|63.118.5[67].\\d+)";
	private final String IP_MATCH = "(127\\.0\\.0\\.1)";

	
    public ServiceFilter() {
        
    }
    
	public void destroy() {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if(((HttpServletRequest)request).getSession().getAttribute("uid") == null) {
			if(((HttpServletRequest)request).getSession().getAttribute("externalPerson") == null) {
				if(!request.getRemoteAddr().matches(IP_MATCH)) {
					String uri = ((HttpServletRequest)request).getRequestURI();
					
					if(uri.contains("/external") || uri.matches("^/opendirectory/(css|img|js/external)/.+$")) {
					
					}
					else {
						UrlMapper urls = new UrlMapper();
						((HttpServletResponse)response).sendRedirect(urls.url("external"));
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}
	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
