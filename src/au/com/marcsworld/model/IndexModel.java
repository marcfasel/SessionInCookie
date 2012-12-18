package au.com.marcsworld.model;

/**
 * Model for the Index Controller.
 * 
 * @author marcfasel
 *
 */
public class IndexModel {

	String userName;
	String password;
	String error;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
