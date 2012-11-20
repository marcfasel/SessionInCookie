package au.com.marcsworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.marcsworld.model.IndexModel;

@Controller
public class LogOutController {
	Logger LOGGER = Logger.getLogger(LogOutController.class);

	@RequestMapping("/logOut.do")
	public String logOut(HttpServletRequest request, HttpServletResponse response, Model model) {
		request.getSession().invalidate();
		model.addAttribute("indexModel", new IndexModel());
		return "index";
	}
}
