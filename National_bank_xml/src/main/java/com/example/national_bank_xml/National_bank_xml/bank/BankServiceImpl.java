package com.example.national_bank_xml.National_bank_xml.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankServiceImpl implements BankService{

	@Autowired
	private BankRepository repository;
	
	@Override
	public Bank findBySwiftCode(String code) {
		
		return repository.findBySwiftCode(code);
	}

	@Override
	public Bank save(Bank bank) {
		// TODO Auto-generated method stub
		return repository.save(bank);
	}

	
}
