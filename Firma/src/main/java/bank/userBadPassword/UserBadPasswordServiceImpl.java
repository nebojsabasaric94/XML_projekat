package bank.userBadPassword;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.user.User;

@Service
@Transactional
public class UserBadPasswordServiceImpl implements UserBadPasswordService{
	
	@Autowired
	private UserBadPasswordRepo repository;

	@Override
	public List<UserBadPassword> findAll() {
		return (List<UserBadPassword>) repository.findAll();
	}

	@Override
	public UserBadPassword findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
		
	}

	@Override
	public UserBadPassword save(UserBadPassword userBadPassword) {
		return repository.save(userBadPassword);
	}

	@Override
	public UserBadPassword findByUserId(User user) {
		

		List<UserBadPassword> badPasswords = (List<UserBadPassword>) repository.findAll(); 
		
		for(int i = 0 ; i < badPasswords.size(); i++){
			if(badPasswords.get(i).getUser().getId() == user.getId()){
				return badPasswords.get(i);
			}
		}
		
		return null;
	}

	@Override
	public boolean checkAccessLocked(Long id) {
		UserBadPassword userBadPassword = repository.findOne(id);
		if(userBadPassword.isAccessLocked()){
			return true;
		} else {
			return false;
		}
	}

}
