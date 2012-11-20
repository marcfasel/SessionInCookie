package au.com.marcsworld;

import java.io.Serializable;
import java.util.Map;

public class CookieSession implements Serializable{
	private static final long serialVersionUID = -779823856918700575L;

	private long lastAccessedTime;
	private Map<String, Object> attributes;
	
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}
	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
