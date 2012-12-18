# SessionInCookie Sample Application #
This application is an implementation of the SessionInCookie pattern in Java. It has a CookieSessionFilter that 
serialises and deserialises the session attributes to and from a cookie.

To demonstrate the functionality a sample SpingMVC application is included. Deploy the application to your favourite
servlet container, and point your browser to [http://localhost:8080/SessionInCookie](http://localhost:8080/SessionInCookie "SessionInCookie").
The application allows you to log in, in which case a user object is placed in the session. You then proceed into the secured part of
the application, which can only be viewed if a user is logged in and this user object is available in the session. This is controlled by 
a SecurityFilter, which issues an HTTP error 401 "Access denied" in case you are not logged in. The 401 page defined in the web.xml is shown
in this case.

The CookieSessionFilter is a base implementation of the pattern as shown in part of my [blog](http://blog.shinetech.com/2012/12/18/simple-session-sharing-in-tomcat-cluster-using-the-session-in-cookie-pattern/) on
the Session-In-Cookie pattern. The project also contains an AdvancedSessionCookieFilter together with an AdvancedCookieSession. This
filter also implements session timeouts, encryption, and signing of the cookie. These topics will be covered by the second part of my blog.
