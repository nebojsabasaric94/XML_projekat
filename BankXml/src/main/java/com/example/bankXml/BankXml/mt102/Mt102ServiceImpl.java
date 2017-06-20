package com.example.bankXml.BankXml.mt102;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class Mt102ServiceImpl implements Mt102Service {
	
	@Autowired
	private Mt102Repository repository;
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Mt102> findAll() {
		return (List<Mt102>) repository.findAll();
	}

	@Override
	public Mt102 findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Mt102 save(Mt102 mt102) {
		return repository.save(mt102);
	}

	@Override
	public Mt102 checkBankAccount(String duznik, String poverilac, boolean obradjen) {
		Query query = entityManager.createQuery("SELECT m FROM Mt102 m WHERE m.obracunskiRacunBankeDuznika=?1 and m.obracunskiRacunBankePoverioca=?2 and m.obradjen=?3");
		query.setParameter(1, duznik);
		query.setParameter(2, poverilac);
		query.setParameter(3, obradjen);
		try{
			Mt102 mt102 = (Mt102) query.getSingleResult();
			return mt102;
		} catch (Exception e) {
			return null;
		}
	}

}
