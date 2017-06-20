package bank.firm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FirmServiceImpl implements FirmService {
	
	@Autowired
	public FirmRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Firma findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Firma save(Firma certificate) {
		return repository.save(certificate);
	}

	@Override
	public Firma findByName(String name) {
		Query query = entityManager.createQuery("SELECT f FROM Firma f WHERE f.name=?1");
		query.setParameter(1, name);
		Firma firm = (Firma) query.getSingleResult();
		return firm;
	}

	@Override
	public List<Firma> findAll() {
		return (List<Firma>) repository.findAll();
	}
	
	

}
