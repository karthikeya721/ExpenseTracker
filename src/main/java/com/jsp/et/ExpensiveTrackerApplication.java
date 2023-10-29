package com.jsp.et;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jsp.et.Repository.ExpenseCategoryRepository;
import com.jsp.et.entity.ExpenseCategory;

@SpringBootApplication
public class ExpensiveTrackerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ExpensiveTrackerApplication.class, args);
	} 

	@Autowired
	private ExpenseCategoryRepository repository;
	@Override
	public void run(String... args) throws Exception {
		//to execute any code at the starting of application  
//		 List<ExpenseCategory> categories=Arrays.asList(
//				 new ExpenseCategory("utilities"),
//				 new ExpenseCategory("Transport"),
//				 new ExpenseCategory("Groceries"),
//				 new ExpenseCategory("Dining Out"),
//				 new ExpenseCategory("HealthCare"),
//				 new ExpenseCategory("Entertainment"),
//				 new ExpenseCategory("Education"),
//				 new ExpenseCategory("Personal Care"),
//				 new ExpenseCategory("Clothing"),
//				 new ExpenseCategory("Home maintenance"),
//				 new ExpenseCategory("Gift and Donations"),
//				 new ExpenseCategory("Savings and Investments"),
//				 new ExpenseCategory("Tax"),
//				 new ExpenseCategory("Others")
//				 );
//		 //store all list elements in the database
//		 repository.saveAll(categories);
		
	}

}
