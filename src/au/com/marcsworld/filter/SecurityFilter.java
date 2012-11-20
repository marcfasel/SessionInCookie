package au.com.marcsworld.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class SecurityFilter implements Filter {
	Logger LOGGER = Logger.getLogger(SecurityFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		LOGGER.debug("Filter called");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpSession session = (httpServletRequest).getSession(false);
		if (session != null) {
			if (null!=session.getAttribute("user")){
				LOGGER.debug("Logged in.");
				chain.doFilter(request, response);
				return;
			}
		} 
		LOGGER.debug("Nobody is logged in.");
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
