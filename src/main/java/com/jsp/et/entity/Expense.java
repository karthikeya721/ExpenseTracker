package com.jsp.et.entity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int expenseid;
	private LocalDate date;
	private double amount;
	private String description;
	
	  @ManyToOne
	  @JoinColumn(name="user_fk")
	  private User user;
	  
	  @ManyToOne
	  @JoinColumn(name="category_fk")
	  private ExpenseCategory expensecategory;
	
}
