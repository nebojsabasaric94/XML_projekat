package com.xml.strukturartgsnaloga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mt910ServiceImpl implements Mt910Service{

	@Autowired
	private Mt910Repo repo;
	
	@Override
	public Mt910 save(Mt910 mt910) {
		// TODO Auto-generated method stub
		return repo.save(mt910);
	}

	
}
