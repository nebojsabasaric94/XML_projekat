package com.example.national_bank_xml.National_bank_xml.firm;

import java.util.List;

public interface FirmService {
	
	public List<Firma> findAll();

	public Firma findOne(Long id);
	
	public Firma findByName(String pib);

	public Firma save(Firma certificate);

	public Firma findByAccount(String racun);

}
