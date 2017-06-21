package com.xml.strukturartgsnaloga;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StrukturaRtgsNalogaServiceImpl implements StrukturaRtgsNalogaService{

	@Autowired
	private StrukturaRtgsNalogaRepo repo;

	@Override
	public StrukturaRtgsNaloga save(StrukturaRtgsNaloga strukturaRtgsNaloga) {
		// TODO Auto-generated method stub
		return repo.save(strukturaRtgsNaloga);
	}
	
}
