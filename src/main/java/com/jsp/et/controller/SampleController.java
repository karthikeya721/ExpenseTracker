package com.jsp.et.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jsp.et.entity.User;

@RestController
public class SampleController {

	@RequestMapping("/msg")
	public String message() {
		return "<h1>Rest Controller Annotation</h1>" ;
		
	}
	@RequestMapping(path="/index",method=RequestMethod.GET)
	public ModelAndView index()
	{
		ModelAndView mv=new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
	@GetMapping("/input/{number}")
	public String getInput(@PathVariable("number")int num) {
		return "Input= " + num;
		
	}
	@PostMapping("/add/{number1}/{number2}")
	public String addition(@PathVariable("number1") int n1,@PathVariable("number2") int n2) {
		return  "Addition = " + (n1 + n2);
		
	}
	@GetMapping("/user")
	public User getObject() {
		//to get structure of user object in json format
		User user=new User();
		return user;
	}
	@GetMapping("/getUser")
	public String displayObject(@RequestBody User user) {
		return user.getFullname();
	}
}
