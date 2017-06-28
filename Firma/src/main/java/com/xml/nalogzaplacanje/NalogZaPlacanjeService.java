package com.xml.nalogzaplacanje;

import java.util.List;

import com.xml.faktura.Faktura;

public interface NalogZaPlacanjeService {

	public NalogZaPlacanje save(NalogZaPlacanje nalogZaPlacanje);
	
	public List<NalogZaPlacanje> findAll();
}
