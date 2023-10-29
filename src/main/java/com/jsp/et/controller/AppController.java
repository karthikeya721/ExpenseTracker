package com.jsp.et.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jsp.et.dto.ExpenseDto;
import com.jsp.et.service.ExpenseService;

@Controller
@RequestMapping("/expense")
public class AppController {

	@RequestMapping("/index")
	public String indexPage() {
		return "TrackerHomePage";
	}
	
	@RequestMapping("/logInPage")
	public String loginPage() {
		return "TrackerLogInPage";
	}
	
	@RequestMapping("/signUpPage")
	public String signUpPage() {
		return "TrackerSignUpPage";
	}
	
	@RequestMapping("/afterLogIn")
	public String afterLogIn() {
		return "TrackerAfterLogin";
	}
	
	@RequestMapping("/addExpenses")
	public String addExpenses() {
		return "TrackerAddExpenses";
	}
	
	@RequestMapping("/viewExpenses")
	public String viewExpenses() {
		return "TrackerTotalExpenses";
	}
	
	@RequestMapping("/totalExpenses")
	public String totalExpenseCounter() {
		return "TrackerTotalExpensesCounter";
	}
	
	@RequestMapping("/filterExpense")
	public String filterExpense() {
		return "TrackerFilterExpenses";
	}
	
	//update Expenses
	@Autowired
	private ExpenseService service;
	
	@RequestMapping("/updateExpense/{id}")
	public ModelAndView updateExpense(@PathVariable("id") int id) {
		
		ExpenseDto dto = service.findByExpenseid(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("UpdateExpense");
		mv.addObject("expense",dto);
		return mv;
	}
	//update user profile
	@RequestMapping("/updateProfile")
	public String updateProfile() {
		return "TrackerUpdateUserProfile";
	}
	
}

//
















