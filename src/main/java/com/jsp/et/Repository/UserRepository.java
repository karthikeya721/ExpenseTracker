package com.jsp.et.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.et.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	
	Optional<User>findByUsernameAndPassword(String username,String password);
//	Optional<User>findById(int userid);
}
