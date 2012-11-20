package au.com.marcsworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecureController {
	Logger LOGGER = Logger.getLogger(SecureController.class);

	@RequestMapping("/secure/secureNext.do")
	public String next(HttpServletRequest request, HttpServletResponse response) {
		return "next";
	}
}
