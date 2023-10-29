package com.jsp.et.service;

import com.jsp.et.dto.UserDto;

public interface UserService {

	int registration(UserDto dto);
	
	//UserDto login(String username,String password)
	UserDto login(UserDto userdto);
	UserDto findByUserId(int userid);
	UserDto updateUserProfile(int userid, UserDto dto);
	int deleteUserProfile(int userid);
}
