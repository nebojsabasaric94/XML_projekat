package com.example.bankXml.BankXml.bank;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankServiceImpl implements BankService {
	
	@Autowired
	private BankRepository repository;

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

}
