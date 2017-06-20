package bank.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bank.security.LoginAttemptService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserRepo repository;

	@Override
	public List<User> findAll() {
		return (List<User>) repository.findAll();
	}

	@Override
	public User findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}

	@Override
	public User save(User user) {
		return repository.save(user);
	}

	@Override
	public User findByUsername(String username) {

		Query query = em.createQuery("SELECT u FROM User u WHERE u.username=?1");
		query.setParameter(1, username);
		User user = (User) query.getSingleResult();
		return user;

	}

	@Override
	public User changePassword(String password, Long id) {
		User user = repository.findOne(id);
		user.setPassword(password);
		return repository.save(user);

	}

	@Override
	public User findByUsernameAndCheckIp(String username, String key) {

		try {
			Query query = em.createQuery("SELECT u FROM User u WHERE u.username=?1");
			query.setParameter(1, username);
			User user = (User) query.getSingleResult();
			return user;
		} catch (Exception e) {
			loginAttemptService.loginFailed(key);
			return null;
		}

	}

	@Override
	public User findByEmail(String email) {
		Query query = em.createQuery("SELECT u FROM User u WHERE u.email=?1");
		query.setParameter(1, email);
		User user = (User) query.getSingleResult();
		return user;
	}

}
