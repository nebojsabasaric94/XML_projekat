package com.xml.mt102;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Mt900Mt102ServiceImpl implements Mt900Service{

	@Autowired
	private Mt900Mt102Repo repo;
	@Override
	public Mt900Mt102 save(Mt900Mt102 mt900) {
		// TODO Auto-generated method stub
		return repo.save(mt900);
	}

}
