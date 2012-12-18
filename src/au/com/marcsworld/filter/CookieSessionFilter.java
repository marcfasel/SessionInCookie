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

import au.com.marcsworld.session.CookieSession;

/**
 * The CookieSessionFilter deserialises and serialises the HTTP session attributes to a cookie. This 
 * allows storage of the session attributes on the client. In a clustered environment this means that
 * sessions are shared between all nodes.
 * 
 * @author marcfasel
 *
 */
public class CookieSessionFilter implements Filter {
	Logger LOGGER = Logger.getLogger(CookieSessionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		LOGGER.debug("doFilter()");

		getSessionFromCookie((HttpServletRequest) request);
		FilterResponseWrapper responseWrapper = new FilterResponseWrapper((HttpServletResponse) response);
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
			String serialisedSession = sessionCookie.getValue();
			try {
				CookieSession cookieSession = (CookieSession) fromString(serialisedSession);
				Map<String, Object> attributes = cookieSession.getAttributes();
				for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
					session.setAttribute(attribute.getKey(), attribute.getValue());
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private void storeSessionInCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		HttpSession session = httpServletRequest.getSession(false);

		@SuppressWarnings("unchecked")
		Enumeration<String> attributeNames = session.getAttributeNames();
		Map<String, Object> attributes = new HashMap<String, Object>();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			attributes.put(attributeName, session.getAttribute(attributeName));
		}
		CookieSession cookieSession = new CookieSession();
		cookieSession.setAttributes(attributes);
		try {
			String serialisedCookieSession = toString((Serializable) cookieSession);
			Cookie newSessionCookie = new Cookie("session", serialisedCookieSession);
			newSessionCookie.setPath("/");
			httpServletResponse.addCookie(newSessionCookie);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private Cookie getSessionCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if ("session".equals(cookie.getName())) {
					return cookie;
				}
			}
		return null;
	}

	private Object fromString(String string) throws IOException, ClassNotFoundException {
		byte[] data = DatatypeConverter.parseBase64Binary(string);
		ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		return object;
	}

	private String toString(Serializable object) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
		return new String(DatatypeConverter.printBase64Binary(byteArrayOutputStream.toByteArray()));
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
