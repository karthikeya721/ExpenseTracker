package com.jsp.et.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jsp.et.dto.ExpenseDto;
import com.jsp.et.dto.ImageDto;
import com.jsp.et.dto.TotalDto;
import com.jsp.et.dto.UserDto;
import com.jsp.et.service.ExpenseService;
import com.jsp.et.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	public ExpenseService expenseService;

// Registration
	@PostMapping("/registration")
	public String registration(@ModelAttribute UserDto userdto, RedirectAttributes ra) {

		if (userdto.getFullname() != "" && userdto.getUsername() != "" && userdto.getEmail() != ""
				&& userdto.getMobile() != "" && userdto.getPassword() != "" && userdto.getRepassword() != "") {
			// display logIn page and redirect request to login page
			int id = userService.registration(userdto);
			if (id != 0) {
				ra.addFlashAttribute("msg", "Registration succussful");
				return "redirect:/expense/logInPage";
			}
		}
		// display registration form
		ra.addFlashAttribute("msg", "Enter valied data");
		return "redirect:/expense/signUpPage";

	}

//Log In
	@PostMapping("/login")
	public String login(@ModelAttribute UserDto userdto, RedirectAttributes ra, HttpServletRequest request) {
		UserDto dto = userService.login(userdto);

		if (dto != null) {
			// to display after login page
			request.getSession().setAttribute("user", dto);
			if(dto.getImage() != null) {
				/**
				 * store image in session object but in the form of string
				 * By using Base64 class present in java.util package - programmer can encode byte 
				 * date to string.
				 */
				request.getSession().setAttribute("image", Base64.getMimeEncoder()
						.encodeToString(dto.getImage().getData()));
			}
			return "redirect:/expense/afterLogIn";
		}
		// if user provides invalid data
		ra.addFlashAttribute("msg", "Please enter proper data");
		return "redirect:/expense/logInPage";
	}

// Add Expenses 
	@PostMapping("/addExpense/{id}")
	public String addExpense(@ModelAttribute ExpenseDto dto, @PathVariable("id") int userid, RedirectAttributes ra) {
		int id = expenseService.addExpense(dto, userid);

		if (id > 0) {
			return "redirect:/viewExpense/" + userid;
		}
		ra.addFlashAttribute("error", "Please Enter Valied Details");
		return "redirect:/expense/addExpenses";
	}

// View Expenses
	@GetMapping("/viewExpense/{id}")
	public String viewExpense(@ModelAttribute("id") int userid, RedirectAttributes ra) {
		List<ExpenseDto> expense = expenseService.viewExpense(userid);
		// isEmpty method will check the elements are present in the object if elements
		// are present it will return false
		if (!expense.isEmpty()) {
			ra.addFlashAttribute("listOfExpense", expense);
			return "redirect:/expense/viewExpenses";
		}
		return "redirect:/expense/afterLogIn";

	}

// Update Expense
	@PostMapping("/updateExpense/{eid}")
	public String updateExpense(@ModelAttribute ExpenseDto dto, @PathVariable("eid") int expenseid,
			HttpServletRequest request) {
		ExpenseDto expense = expenseService.updateExpense(dto, expenseid);
		if (expense != null) {
			UserDto userDto = (UserDto) request.getSession().getAttribute("user");
			return "redirect:/viewExpense/" + userDto.getUserid();
		}
		return "redirect:/expense/afterLogIn";

	}

// Delete Expense
	@GetMapping("/deleteExpense/{eid}")
	public String deleteExpense(@PathVariable("eid") int expenseid, HttpServletRequest request) {
		int id = expenseService.deleteExpense(expenseid);
		if (id != 0) {
			UserDto dto = (UserDto) request.getSession().getAttribute("user");
			return "redirect:/viewExpense/" + dto.getUserid();
		}
		return "redirect:/expense/afterLogIn";

	}

	@GetMapping("findByExpenseid/{id}")
	public ResponseEntity<ExpenseDto> findByExpenseid(@PathVariable("id") int expenseid) {
		ExpenseDto dto = expenseService.findByExpenseid(expenseid);
		if (dto != null) {
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	}

	// filter method
	@GetMapping("/filter/{uid}")
	public String filterBasedOnDateCategoryAmount(@ModelAttribute ExpenseDto dto, @PathVariable("uid") int userid,
			RedirectAttributes ra) {
		if (dto.getDate() != "") {
			List<ExpenseDto> filterByDate = expenseService.filterBasedOnDate(dto, userid);
			ra.addFlashAttribute("listOfExpense", filterByDate);
			return "redirect:/expense/viewExpenses";
		}

		else if (dto.getCategory() != "") {
			List<ExpenseDto> filterByCategory = expenseService.filterBasedOnCategory(dto, userid);
			ra.addFlashAttribute("listOfExpense", filterByCategory);
			return "redirect:/expense/viewExpenses";
			
		} else if (!dto.getRange().equalsIgnoreCase("0")) {
			List<ExpenseDto> filterByAmount = expenseService.filterBasedOnAmount(userid, dto.getRange());
			ra.addFlashAttribute("listOfExpense", filterByAmount);
			return "redirect:/expense/viewExpenses";
		}
		return "redirect:/expense/afterLogIn";

	}

	@PostMapping("filterBasedOnDate/{id}")
	public ResponseEntity<List<ExpenseDto>> filterBasedOnDate(@RequestBody ExpenseDto dto,
			@PathVariable("id") int userid) {
		List<ExpenseDto> expense = expenseService.filterBasedOnDate(dto, userid);
		if (expense != null) {
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	}

	@PostMapping("/filterBasedOnAmmount/{id}/{ammount}")
	public ResponseEntity<List<ExpenseDto>> filterBasedOnAmount(@PathVariable("id") int userid,
			@PathVariable("ammount") String amount) {
		List<ExpenseDto> expense = expenseService.filterBasedOnAmount(userid, amount);
		if (expense != null) {
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	}

	@PostMapping("filterBasedOnCategoryDate/{id}")
	public ResponseEntity<List<ExpenseDto>> filterBasedOnCategoryDate(@RequestBody ExpenseDto dto,
			@PathVariable("id") int userid) {
		List<ExpenseDto> expense = expenseService.filterBasedOnCategoryDate(dto, userid);
		if (expense != null) {
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	}

	@PostMapping("filterBasedOnCategoryAmount/{id}/{amount}")
	public ResponseEntity<List<ExpenseDto>> filterBasedOnCategoryAmount(@RequestBody ExpenseDto dto,
			@PathVariable("id") int userid, @PathVariable("amount") String amount) {
		List<ExpenseDto> expense = expenseService.filterBasedOnCategoryAmount(dto, userid, amount);
		if (expense != null) {
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

	}

	@GetMapping("filterBasedOnCategory/{id}")
	public ResponseEntity<List<ExpenseDto>> filterBasedOnCategory(@RequestBody ExpenseDto dto,
			@PathVariable("id") int userid) {
		List<ExpenseDto> expense = expenseService.filterBasedOnCategory(dto, userid);
		if (expense != null) {
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(expense);
	}

// total expenses
	@GetMapping("total/{id}")
	public String getTotalExpense(@ModelAttribute TotalDto total, @PathVariable("id") int userid, Model m) {
		List<ExpenseDto> dateDto = expenseService.filterExpenseBasedOnDate(LocalDate.parse(total.getStart()),
				LocalDate.parse(total.getEnd()), userid);
		m.addAttribute("listOfExpense", dateDto);
		m.addAttribute("total", dateDto.stream().mapToDouble(t -> t.getAmount()).sum());

		return "TrackerTotalExpenses";

	}

	@GetMapping("findByUserId/{id}")
	public ResponseEntity<UserDto> findByUserId(@PathVariable("id") int userid) {
		UserDto user = userService.findByUserId(userid);
		if (user != null) {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@DeleteMapping("deleteUserProfile/{id}")
	public ResponseEntity<Integer> deleteUserProfile(@PathVariable("id") int userid) {
		int status = userService.deleteUserProfile(userid);
		if (status != 0) {
			return ResponseEntity.status(HttpStatus.OK).body(status);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);

	}
//Update User profile
	@PostMapping("/updateUserProfile/{id}")
	public String updateUserProfile(@PathVariable("id") int id, @ModelAttribute UserDto dto,
			@RequestParam("imageFile") MultipartFile file, HttpServletRequest request, RedirectAttributes ra) {
		try {
			/**
			 * retrieve UserDto object from session object, store at the time of login
			 */
			UserDto fromSession = (UserDto) request.getSession().getAttribute("user");
			//if user already have uploaded profile photo then update the photo
			if(fromSession.getImage() != null) {
				//update login
				fromSession.getImage().setData(file.getBytes());
				dto.setImage(fromSession.getImage());
				//store same image in session object
				request.getSession().setAttribute("image", Base64.getMimeEncoder()
						.encodeToString(dto.getImage().getData()));
			}
			else {
			ImageDto imageDto = new ImageDto();
			imageDto.setData(file.getBytes());
			dto.setImage(imageDto);
			}
			userService.updateUserProfile(id, dto);
			ra.addFlashAttribute("msg", "Profile Updated");
			return "redirect:/expense/logInPage";
		}
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/expense/afterLogIn";
	}

}
