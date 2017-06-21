package com.xml.mt102;

import java.util.List;

public interface Mt102Service {
	
	public List<Mt102> findAll();
	
	public Mt102 findOne(Long id);
	
	public Mt102 save(Mt102 mt102);
	
	public Mt102 checkBankAccount(String duznik, String poverilac,boolean obradjen);

}
