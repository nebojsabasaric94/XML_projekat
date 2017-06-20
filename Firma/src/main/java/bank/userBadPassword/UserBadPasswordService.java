package bank.userBadPassword;

import java.util.List;

import bank.user.User;

public interface UserBadPasswordService {

	public List<UserBadPassword> findAll();
	
	public UserBadPassword findOne(Long id);
	
	public void delete(Long id);
	
	public UserBadPassword save(UserBadPassword userBadPassword);
	
	public UserBadPassword findByUserId(User user);
	
	public boolean checkAccessLocked(Long id);
	
}
