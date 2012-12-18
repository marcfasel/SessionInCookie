package au.com.marcsworld.session;

import java.io.Serializable;

/**
 * An advanced version of the @CookieSession that stores the last time the
 * session was accessed.
 * 
 * @author marcfasel
 *
 */
public class AdvancedCookieSession extends CookieSession implements Serializable{
	private static final long serialVersionUID = -779823856918700575L;

	private long lastAccessedTime;

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
}
