package au.com.marcsworld.session;

import java.io.Serializable;
import java.util.Map;

/**
 * Container class to store HTTP session attribute map.
 * 
 * @author marcfasel
 *
 */
public class CookieSession implements Serializable{
	private static final long serialVersionUID = -779823856918700575L;

	private Map<String, Object> attributes;
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
