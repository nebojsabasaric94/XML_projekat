package com.xml.bank;

public interface BankService {

	public Bank findBySwiftCode(String code);
	
	public Bank save(Bank bank);
}
