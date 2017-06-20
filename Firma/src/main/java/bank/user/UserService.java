package bank.user;

import java.util.List;

public interface UserService {
	
	public List<User> findAll();
	
	public User findOne(Long id);
	
	public void delete(Long id);
	
	public User save(User user);
	
	public User findByUsername(String username);
	
	public User findByUsernameAndCheckIp(String username, String key);
	
	public User findByEmail(String email);
	
	public User changePassword(String password, Long id);
	

}
