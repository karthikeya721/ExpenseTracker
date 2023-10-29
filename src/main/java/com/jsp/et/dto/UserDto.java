package com.jsp.et.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//Data transfer object it will transfer data from controller to service
//it is use full to store data which entered by user in frontend files
public class UserDto {

	private int userid;
	private String fullname;
	private String username;
	private String email;
	private String mobile;
	private String password;
	private String repassword;
	private ImageDto image;
}
