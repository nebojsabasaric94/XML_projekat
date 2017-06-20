package bank.faktura;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FakturaServiceImpl implements FakturaService {

	@Autowired
	FakturaRepo repository;
	
	@Override
	public List<Faktura> findAll() {
		return (List<Faktura>) repository.findAll();
	}

	@Override
	public Faktura save(Faktura faktura) {
		return repository.save(faktura);
	}

	@Override
	public Faktura findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		repository.delete(id);
	}
	
	

}
