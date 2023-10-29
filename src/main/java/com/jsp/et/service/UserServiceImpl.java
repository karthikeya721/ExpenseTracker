package com.jsp.et.service;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.et.Repository.UserRepository;
import com.jsp.et.dto.ImageDto;
import com.jsp.et.dto.UserDto;
import com.jsp.et.entity.Image;
import com.jsp.et.entity.User;
//business logics
@Service
public class UserServiceImpl implements UserService{
@Autowired
	private UserRepository userRepository;
	@Override
	public int registration(UserDto dto) {
		 //create object of an entity class
		
		User userdb=new User();
		//transfer data from dto to entity object
		/*
		 * Basic way 
		 * userdb.setUserName(dto.getUserName);
		 * userdb.setFullName(dto.getFullName);
		 */
		BeanUtils.copyProperties(dto, userdb);
       if(dto.getPassword().equals(dto.getRepassword())) {
    	   return userRepository.save(userdb).getUserid();
       }
       return 0;
	}
	 
	@Override
	public UserDto login(UserDto userdto) {
		Optional<User>fingByUsernameAndPassword = 
				userRepository.findByUsernameAndPassword(userdto.getUsername(),userdto.getPassword());
		UserDto verifiedUser=new UserDto();
		
		if(fingByUsernameAndPassword.isPresent()) {
		 User userdb = fingByUsernameAndPassword.get();
		 BeanUtils.copyProperties(userdb, verifiedUser);
		 // to store image dto object into userdto
		 if(userdb.getImage() != null) {
			 ImageDto imageDto = new ImageDto();
			 BeanUtils.copyProperties(userdb.getImage(), imageDto);
			 verifiedUser.setImage(imageDto);
			 }
		return verifiedUser;
	}
	return null;
}

	@Override
	public UserDto findByUserId(int userid) {
		 UserDto dto=new UserDto();
		 BeanUtils.copyProperties(userRepository.findById(userid).get(),dto);
		return dto;
	}

	@Override
	public UserDto updateUserProfile(int userid, UserDto userDto) {
		 User user = userRepository.findById(userid).get();
//		 user.setFullname(userDto.getFullname());
//		 user.setPassword(userDto.getPassword());
		 user.setMobile(userDto.getMobile());
		 user.setUsername(userDto.getUsername());
		 // to add image object into user object
		 System.err.println(userDto.getImage().getData().length +" length");
		 if(userDto.getImage().getData().length != 0) {
			 System.out.println(userDto.getImage().getData().length);
			 Image image = new Image();
			 BeanUtils.copyProperties(userDto.getImage(), image);
			 user.setImage(image);
		 }
		 UserDto updated = new UserDto();
		 BeanUtils.copyProperties(userRepository.save(user), updated);
		return updated;
	}

	@Override
	public int deleteUserProfile(int userid) {
		 Optional<User> user = userRepository.findById(userid);
		 if(user.isPresent()) {
			 userRepository.delete(user.get());
			 return 1;
		 }
		return 0;
	}
}
