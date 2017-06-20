package com.example.bankXml.BankXml.bank;

import java.util.List;

public interface BankService {
	
	public List<Bank> findAll();
	
	public Bank findOne(Long id);
	
	public Bank save(Bank bank);

}
