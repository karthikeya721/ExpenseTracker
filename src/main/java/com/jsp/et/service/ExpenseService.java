package com.jsp.et.service;


import java.time.LocalDate;
import java.util.List;

import com.jsp.et.dto.ExpenseDto;

public interface ExpenseService {

	int addExpense(ExpenseDto dto,int userid);
	
	List<ExpenseDto> viewExpense(int userid);
	
	ExpenseDto findByExpenseid(int expenseid);
	
	ExpenseDto updateExpense(ExpenseDto dto,int expenseid);
	
	int deleteExpense(int expenseid);
	
	List<ExpenseDto> filterBasedOnDateCategoryAmount(ExpenseDto dto,int userid);
	
	List<ExpenseDto> filterBasedOnDate(ExpenseDto dto,int userid);
	
	List<ExpenseDto> filterBasedOnAmount(int userid, String amount);
	
	List<ExpenseDto> filterBasedOnCategoryDate(ExpenseDto dto,int userid);
	
	List<ExpenseDto> filterBasedOnCategoryAmount(ExpenseDto dto,int userid,String amount);
	
	List<ExpenseDto> filterBasedOnCategory(ExpenseDto dto,int userid);
	
	List<ExpenseDto> filterExpenseBasedOnDate(LocalDate start,LocalDate end,int userid);
}
