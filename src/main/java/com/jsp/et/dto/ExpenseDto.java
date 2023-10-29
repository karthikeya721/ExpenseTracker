package com.jsp.et.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseDto {

	private int expenseid;
	private String date;
	private double amount;
	private String description;
	// to take range of an amount select by user
	private String range;
	//Creating ExpensesCaregoryDto reference variable may create difficulties in service layer hence we go with 
	//String
	private String category;
}
