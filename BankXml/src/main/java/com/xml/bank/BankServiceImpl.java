package com.xml.bank;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankServiceImpl implements BankService {
	
	@Autowired
	private BankRepository repository;
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Bank> findAll() {
		return (List<Bank>) repository.findAll();
	}

	@Override
	public Bank findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Bank save(Bank bank) {
		return repository.save(bank);
	}

	@Override
	public Bank findByObracunskiRacunBanke(String obracunskiRacunBanke) {
		Query query = entityManager.createQuery("SELECT b FROM Bank b WHERE b.obracunskiRacunBanke=?1");
		query.setParameter(1, obracunskiRacunBanke);
		Bank bank = (Bank) query.getSingleResult();
		return bank;
	}

}
