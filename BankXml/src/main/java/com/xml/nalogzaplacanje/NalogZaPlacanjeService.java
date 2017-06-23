package com.xml.nalogzaplacanje;

import java.util.List;

public interface NalogZaPlacanjeService {

	public NalogZaPlacanje save(NalogZaPlacanje nalogZaPlacanje);
	
	public List<NalogZaPlacanje> findAll();
}
