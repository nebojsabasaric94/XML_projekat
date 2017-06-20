package bank.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleRepo repository;
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Role> findAll() {
		return (List<Role>) repository.findAll();
	}

	@Override
	public Role findByEnumRole(EnumRole enumRole) {
		Query query = em.createQuery("SELECT r FROM Role r WHERE r.enumRole=?1");
		query.setParameter(1, enumRole);
		Role role = (Role) query.getSingleResult();
		return role;
	}

}
