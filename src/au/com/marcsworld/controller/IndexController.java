package au.com.marcsworld.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.marcsworld.model.IndexModel;

/**
 * First controller to be hit.
 * 
 * @author marcfasel
 *
 */
@Controller
public class IndexController {
	Logger LOGGER = Logger.getLogger(IndexController.class);

	@RequestMapping("/index.do")
	public String serveIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		LOGGER.debug("serveIndex()");
		model.addAttribute("indexModel",new IndexModel());
		return "index";
	}
}
