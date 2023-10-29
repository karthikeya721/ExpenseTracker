package com.jsp.et.service;

import java.time.LocalDate;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jsp.et.Repository.ExpenseCategoryRepository;
import com.jsp.et.Repository.ExpenseRepository;
import com.jsp.et.Repository.UserRepository;
import com.jsp.et.dto.ExpenseDto;
import com.jsp.et.entity.Expense;
import com.jsp.et.entity.ExpenseCategory;
import com.jsp.et.entity.User;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private ExpenseCategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	/*
	 * Expense table contains two foreign keys-categoryId & UserId To insert value
	 * in foreign keys columns ,firstly programmer have to verify user and category
	 * then need to retrieve it by using name and id after that insert into Expense
	 * table
	 */
	public int addExpense(ExpenseDto dto, int userid) {

		// Verification and retrieval of category by using its name
		Optional<ExpenseCategory> category = categoryRepository.findByCategory(dto.getCategory());

		// verification and retrieval of user by using its id
		Optional<User> user = userRepository.findById(userid);
		// if both are valid then insert records in a table
		if (category.isPresent() && user.isPresent()) {
			Expense expense = new Expense();
			BeanUtils.copyProperties(dto, expense);
			// convert string of dto to localDate of entity
			expense.setDate(LocalDate.parse(dto.getDate()));
			expense.setExpensecategory(category.get());
			expense.setUser(user.get());
			
			Expense expense2 = expenseRepository.save(expense);
			return expense2.getExpenseid();
		}
		return 0;
	}

	/*
	 * to get all the expense based on userid because every user have different
	 * expense retrieve expense by using userid
	 */
	@Override
	public List<ExpenseDto> viewExpense(int userid) {
		// 1. find user details in user table based on id then retrieve object

		User user = userRepository.findById(userid).get();

		// created user defined method in expenseRepository to access expense by user
		// user details

		/*
		 * findByUser list of expense entity object,so to copy data from expense entity
		 * list to expensedto list make use of stream api or foreach loop
		 */
		return expenseRepository.findByUser(user).stream().map(t -> {
			ExpenseDto dto = new ExpenseDto();
			BeanUtils.copyProperties(t, dto);
			// to store category from category table to expensedto
			/*
			 * t.getExpensecategory() gives object of Expensecategory entity class
			 */
			dto.setCategory(t.getExpensecategory().getCategory());
			// convert local date of entity class into String of dto
			dto.setDate(t.getDate().toString());
			return dto;
		}).collect(Collectors.toList());
	};

	/*
	 * to update expense details
	 */
	@Override
	public ExpenseDto updateExpense(ExpenseDto dto, int expenseid) {
		// 1.Find expense by usingits id
		Expense expense = expenseRepository.findById(expenseid).get();
		// 2.verification
		if (expense != null) {
			// 3.update data is retrieved object of expense
			expense.setAmount(dto.getAmount());
//			expense.setDate(dto.getDate());
			// convert string to date
			expense.setDate(LocalDate.parse(dto.getDate()));
			expense.setDescription(dto.getDescription());

			// 4.find category from category table based on its name then update is expense
			ExpenseCategory category = categoryRepository.findByCategory(dto.getCategory()).get();
			expense.setExpensecategory(category);

			// 5.update expense by using save method
			ExpenseDto updated = new ExpenseDto();
			updated.setCategory(category.getCategory());
			BeanUtils.copyProperties(expenseRepository.save(expense), updated);
			return updated;
		}
		return null;
	}

	/*
	 * to delete expense based on id
	 */
	@Override
	public int deleteExpense(int expenseid) {
		// 1.find expense based on id
		Optional<Expense> expenseDB = expenseRepository.findById(expenseid);
		// 2.verification
		if (expenseDB.isPresent()) {
			// 3.deletion
			expenseRepository.delete(expenseDB.get());
			return 1;
		}

		return 0;
	}

	/*
	 * to get expense based on its id
	 */
	@Override
	public ExpenseDto findByExpenseid(int expenseid) {
		Optional<Expense> expensedb = expenseRepository.findById(expenseid);
		if (expensedb.isPresent()) {
			ExpenseDto dto = new ExpenseDto();
			BeanUtils.copyProperties(expensedb.get(), dto);
			dto.setCategory(expensedb.get().getExpensecategory().getCategory());
			//convert local date to String
			dto.setDate(expensedb.get().getDate().toString());
			return dto;
		}
		return null;
	}

	/*
	 * it will retrive data from db based on matching category ammount and data make
	 * use of filter method from stream api
	 */
	@Override
	public List<ExpenseDto> filterBasedOnDateCategoryAmount(ExpenseDto dto, int userid) {
		/*
		 * expense call viewexpense method because its contains the logics to get all
		 * expense of respective user,so programmer have to just filter expense of user
		 * as per requirement
		 */
		return viewExpense(userid).stream().filter(t -> t.getDate().equals(dto.getDate())
				&& t.getAmount() == dto.getAmount() && t.getCategory().equals(dto.getCategory())).toList();
	}

	@Override
	public List<ExpenseDto> filterBasedOnDate(ExpenseDto dto, int userid) {
		return viewExpense(userid).stream().filter(t -> t.getDate().equals(dto.getDate())).toList();
	}

	@Override
	public List<ExpenseDto> filterBasedOnAmount(int userid, String amount ) {
		String[] cash = amount.split("-");
		int firstValue = Integer.parseInt(cash[0]);
		int secondValue = Integer.parseInt(cash[1]);
		return viewExpense(userid).stream().filter(t -> t.getAmount() >= firstValue && t.getAmount() <= secondValue)
				.collect(Collectors.toList());
	}

	@Override
	public List<ExpenseDto> filterBasedOnCategoryDate(ExpenseDto dto, int userid) {
		return viewExpense(userid).stream()
				.filter(t -> t.getCategory().equals(dto.getCategory()) && t.getDate().equals(dto.getDate())).toList();
	}

	@Override
	public List<ExpenseDto> filterBasedOnCategoryAmount(ExpenseDto dto, int userid, String amount) {
		String[] cash = amount.split("-");
		int firstValue = Integer.parseInt(cash[0]);
		int secondValue = Integer.parseInt(cash[1]);
		return viewExpense(userid).stream().filter(t -> t.getCategory().equals(dto.getCategory())
				&& t.getAmount() >= firstValue && t.getAmount() <= secondValue).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseDto> filterBasedOnCategory(ExpenseDto dto, int userid) {
		return viewExpense(userid).stream().filter(t -> t.getCategory().equals(dto.getCategory())).toList();
	}

	// it will find total of expense between given data for respective user
	
	
	@Override
	public List<ExpenseDto> filterExpenseBasedOnDate(LocalDate start, LocalDate end, int userid) {
		/*
		 * 1. get all expenses for respective user 
		 * 2. make use of stream api to filter expense based on given start and end date
		 * */
		return viewExpense(userid).stream().filter(t -> {
			LocalDate date = LocalDate.parse(t.getDate());
			return !date.isBefore(start) && !date.isAfter(end);
 		}).collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	

}
