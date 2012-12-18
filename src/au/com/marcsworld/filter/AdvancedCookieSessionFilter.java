package au.com.marcsworld.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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

import au.com.marcsworld.session.AdvancedCookieSession;

/**
 * The AdvancedCookieSessionFilter is an advanced version of the @CookieSessionFilter. It also allows storage
 * of the HTTP session in a cookie on the client. In addition, the AdvancedCookieSessionFilter checks session
 * timeout, encrypts and signs the cookie to make it more secure.
 *  
 * @author marcfasel
 *
 */
public class AdvancedCookieSessionFilter implements Filter {
	Logger LOGGER = Logger.getLogger(AdvancedCookieSessionFilter.class);
	private static String SECRET = "MySuperSecretKey"; // secret key length must
														// be 16

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
			String encryptedSessionPlusSignature = sessionCookie.getValue();
			try {
				String signature = encryptedSessionPlusSignature.substring(encryptedSessionPlusSignature.length() - 40);
				String encryptedSession = encryptedSessionPlusSignature.substring(0,
						encryptedSessionPlusSignature.length() - 40);
				// check signature
				if (!signature.equals(calculateSignature(encryptedSession))) {
					LOGGER.error("Session has been tampered with");
				}
				String serialisedSession = decrypt(encryptedSession);
				AdvancedCookieSession cookieSession = (AdvancedCookieSession) fromString(serialisedSession);
				// Check Session expiry
				if (cookieSession.getLastAccessedTime() + (session.getMaxInactiveInterval() * 1000) < new Date()
						.getTime()) {
					LOGGER.error("Session expired");
				}
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

		Enumeration<String> attributeNames = session.getAttributeNames();
		Map<String, Object> attributes = new HashMap<String, Object>();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			attributes.put(attributeName, session.getAttribute(attributeName));
		}
		AdvancedCookieSession cookieSession = new AdvancedCookieSession();
		cookieSession.setAttributes(attributes);
		cookieSession.setLastAccessedTime(new Date().getTime());
		try {
			String serialisedCookieSession = toString((Serializable) cookieSession);
			String signature = calculateSignature(serialisedCookieSession);
			Cookie newSessionCookie = new Cookie("session", encrypt(serialisedCookieSession) + signature);
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

	private String calculateSignature(String serialisedSession) {
		String hash = null;
		MessageDigest cript;
		try {
			cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(serialisedSession.getBytes("utf8"));
			hash = new BigInteger(1, cript.digest()).toString(16);
			// Pad to 40 chars
			hash = String.format("%1$40s", hash);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return hash;
	}

	public String encrypt(String string) {
		byte[] ciphertext = null;
		try {
			SecretKey key = new SecretKeySpec(SECRET.getBytes(), "AES");
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.ENCRYPT_MODE, key);
			ciphertext = aes.doFinal(string.getBytes());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new String(ciphertext);
	}

	public String decrypt(String encryptedString) {
		String cleartext=null;
		try {
			SecretKey key = new SecretKeySpec(SECRET.getBytes(), "AES");
			Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, key);
			cleartext = new String(aes.doFinal(encryptedString.getBytes()));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return cleartext;
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
