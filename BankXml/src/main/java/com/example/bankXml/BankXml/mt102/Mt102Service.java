package com.example.bankXml.BankXml.mt102;

import java.util.List;

import com.example.bankXml.BankXml.mt102.Mt102;

public interface Mt102Service {
	
	public List<Mt102> findAll();
	
	public Mt102 findOne(Long id);
	
	public Mt102 save(Mt102 mt102);
	
	public Mt102 checkBankAccount(String duznik, String poverilac,boolean obradjen);

}
