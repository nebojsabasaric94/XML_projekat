package com.xml.strukturartgsnaloga;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Mt900ServiceImpl implements Mt900Service{

	@Autowired
	private Mt900Repo repo;
	
	@Override
	public Mt900 save(Mt900 mt900) {
		// TODO Auto-generated method stub
		return repo.save(mt900);
	}

}
