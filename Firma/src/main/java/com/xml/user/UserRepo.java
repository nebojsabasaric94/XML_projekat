package com.xml.user;

import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, Long> {
	
}
