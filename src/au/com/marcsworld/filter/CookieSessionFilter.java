package au.com.marcsworld.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import au.com.marcsworld.CookieSession;

public class CookieSessionFilter implements Filter {
	Logger LOGGER = Logger.getLogger(CookieSessionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException {
		LOGGER.debug("doFilter()");
		
		getSessionFromCookie((HttpServletRequest) request);
		FilterResponseWrapper responseWrapper = new FilterResponseWrapper((HttpServletResponse)response);
		chain.doFilter(request, responseWrapper);
		storeSessionInCookie((HttpServletRequest) request, responseWrapper);
		responseWrapper.flushBuffer();
	}

	private void getSessionFromCookie(HttpServletRequest httpServletRequest) throws IOException {
		Cookie sessionCookie = getSessionCookie(httpServletRequest);
		// Kill existing session and get a fresh one
		HttpSession session = httpServletRequest.getSession();
		session.invalidate();
		session = httpServletRequest.getSession();
		if (null != sessionCookie) {
			extractSession(sessionCookie, session);
		}
	}

	private void extractSession(Cookie sessionCookie, HttpSession session) throws IOException {
		String serialisedSession = sessionCookie.getValue(); 
		try {
			CookieSession cookieSession = (CookieSession) fromString(serialisedSession);
			Map<String, Object> attributes = cookieSession.getAttributes();
			for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
				session.setAttribute(attribute.getKey(), attribute.getValue());
			}
		} catch (ClassNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void storeSessionInCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		HttpSession session = httpServletRequest.getSession(false);

		Enumeration<String> attributeNames = session.getAttributeNames();
		Map<String, Object> attributes = new HashMap<String, Object>();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			attributes.put(attributeName, session.getAttribute(attributeName));
		}
		try {
			String serialisedAttributes = toString((Serializable) attributes);
			Cookie	newSessionCookie = new Cookie("session", serialisedAttributes);
			newSessionCookie.setPath("/");
			httpServletResponse.addCookie(newSessionCookie);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private Cookie getSessionCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie ck : cookies) {
				if ("session".equals(ck.getName())) {
					return ck;
				}
			}
		return null;
	}

	private Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = DatatypeConverter.parseBase64Binary(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	private String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(DatatypeConverter.printBase64Binary(baos.toByteArray()));
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}


}
