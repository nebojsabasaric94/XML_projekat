package com.xml.nalogzaplacanje;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public interface NalogZaPlacanjeService {

	public NalogZaPlacanje save(NalogZaPlacanje nalogZaPlacanje);
	
	public List<NalogZaPlacanje> findAll();
	
	public List<NalogZaPlacanje> findForIzvod(XMLGregorianCalendar datum,
			String brojRacuna, int brStavkiPoPreseku, int redniBrojPreseka);
}
