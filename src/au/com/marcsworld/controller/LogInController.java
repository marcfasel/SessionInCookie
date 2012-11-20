package au.com.marcsworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.marcsworld.User;
import au.com.marcsworld.model.IndexModel;

@Controller
public class LogInController {
	Logger LOGGER = Logger.getLogger(LogInController.class);

	@RequestMapping("/logIn.do")
	public String logIn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute IndexModel indexModel, Model model) {
		if (isValid(indexModel)){
			storeUserInSession(indexModel, request);
			model.addAttribute("userName", indexModel.getUserName());
			return "loggedIn";
		}
		// Login failed
		indexModel.setError("Wrong User Id/ Password combination");
		return "index";
	}
	
	private boolean isValid(IndexModel indexModel){
		return ("Steve".equals(indexModel.getUserName()) && "password".equals(indexModel.getPassword()));
	}
	
	private void storeUserInSession(IndexModel indexModel,HttpServletRequest request ){
		HttpSession session = request.getSession();
		User user = new User();
		user.setUserName(indexModel.getUserName());
		user.setPassword(indexModel.getPassword());
		user.setStreetNumber("303");
		user.setStreet("Collins Ave");
		user.setCity("Melbourne");
		user.setState("Vic");
		user.setCode("3000");
		session.setAttribute("user", user);

	}
}
