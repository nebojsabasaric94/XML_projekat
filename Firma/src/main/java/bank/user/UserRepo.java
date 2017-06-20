package bank.user;

import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, Long> {
	
}
