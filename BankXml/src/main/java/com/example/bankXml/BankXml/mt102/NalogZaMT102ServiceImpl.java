package com.example.bankXml.BankXml.mt102;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NalogZaMT102ServiceImpl implements NalogZaMT102Service{
	
	@Autowired
	private NalogZaMT102Repository repository;

	@Override
	public NalogZaMT102 save(NalogZaMT102 nalogZaMT102) {
		return repository.save(nalogZaMT102);
	}

}
