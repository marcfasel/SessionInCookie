package au.com.marcsworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The SecureController is in the secure part of the web application.
 * It can only be accessed if the user is logged in. This is checked by
 * the @SecurityFilter.
 * 
 * @author marcfasel
 *
 */
@Controller
public class SecureController {
	Logger LOGGER = Logger.getLogger(SecureController.class);

	@RequestMapping("/secure/secureNext.do")
	public String next(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("user", request.getSession().getAttribute(("user")));
		return "next";
	}
}
