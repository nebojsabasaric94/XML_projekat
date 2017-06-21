package com.xml.mt102;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mt910Mt102ServiceImpl implements Mt910Service{
	
	@Autowired
	private Mt910Mt102Repo repo;

	@Override
	public Mt910Mt102 save(Mt910Mt102 mt910) {
		// TODO Auto-generated method stub
		return repo.save(mt910);
	}

}
