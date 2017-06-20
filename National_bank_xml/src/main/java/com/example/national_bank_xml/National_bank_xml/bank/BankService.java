package com.example.national_bank_xml.National_bank_xml.bank;

public interface BankService {

	public Bank findBySwiftCode(String code);
	
	public Bank save(Bank bank);
}
