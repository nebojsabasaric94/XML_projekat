package com.xml.nalogzaplacanje;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NalogZaPlacanjeServiceImpl implements NalogZaPlacanjeService{

	@Autowired
	private NalogZaPlacanjeRepo nalogZaPlacanjeRepo;

	@Override
	public NalogZaPlacanje save(NalogZaPlacanje nalogZaPlacanje) {
		// TODO Auto-generated method stub
		return nalogZaPlacanjeRepo.save(nalogZaPlacanje);
	}

	@Override
	public List<NalogZaPlacanje> findAll() {
		return (List<NalogZaPlacanje>) nalogZaPlacanjeRepo.findAll();
	}


	@Override
	public List<NalogZaPlacanje> findForIzvod(XMLGregorianCalendar datum, String brojRacuna, int brStavkiPoPreseku,
			int redniBrojPreseka) {
		return (List<NalogZaPlacanje>) nalogZaPlacanjeRepo.findAll();

	}
	
	
	
}
