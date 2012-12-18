package au.com.marcsworld.domain;

import java.io.Serializable;

/**
 * Sample user class to be stored in the session. This is used as an example
 * of data serialised and deserialised by the @CookieSessionFilter and the 
 * @AdvancedCookieSessionFilter.
 *  
 * @author marcfasel
 *
 */
public class User implements Serializable{
	private static final long serialVersionUID = 2828589506913268163L;

	String userName;
	String password;
	String companyName;
	String street;
	String city;
	String state;
	String code;
	String country;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
