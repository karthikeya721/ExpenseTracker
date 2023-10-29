package com.jsp.et.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LogoutController {

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, RedirectAttributes ra) {
		
		HttpSession session = request.getSession();
		// to close the session object
		session.invalidate();
		ra.addFlashAttribute("msg", "Logout Sucecssfully");
		
		return "redirect:/expense/logInPage";
	}
}
