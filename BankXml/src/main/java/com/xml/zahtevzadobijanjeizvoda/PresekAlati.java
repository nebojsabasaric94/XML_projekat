package com.xml.zahtevzadobijanjeizvoda;

import java.math.BigDecimal;
import java.util.List;

import com.xml.nalogzaplacanje.NalogZaPlacanje;

public class PresekAlati {
	
	
	public static int IzracunajBrojPromenaUKorist(List<NalogZaPlacanje> nalozi, String brRacuna){
		int counter = 0;
		for(int i=0; i<nalozi.size(); i++){
			if(nalozi.get(i).getRacunPoverioca().equals(brRacuna))
				counter++;
		}
		return counter;
	}
	
	
	public static int IzracunajBrojPromenaNaTeret(List<NalogZaPlacanje> nalozi, String brRacuna){
		int counter = 0;
		for(int i=0; i<nalozi.size(); i++){
			if(nalozi.get(i).getRacunDuznika().equals(brRacuna))
				counter++;
		}
		return counter;
	}
	
	
	public static BigDecimal IzracunajUkupnoUKorist(List<NalogZaPlacanje> nalozi, String brRacuna){
		BigDecimal sum = BigDecimal.valueOf(0);
		for(int i=0; i<nalozi.size(); i++){
			if(nalozi.get(i).getRacunPoverioca().equals(brRacuna))
				sum =  sum.add(nalozi.get(i).getIznos());
		}
		return sum;
	}
	
	public static BigDecimal IzracunajUkupnoNaTeret(List<NalogZaPlacanje> nalozi, String brRacuna){
		BigDecimal sum = BigDecimal.valueOf(0);
		for(int i=0; i<nalozi.size(); i++){
			if(nalozi.get(i).getRacunDuznika().equals(brRacuna))
				sum =  sum.add(nalozi.get(i).getIznos());
		}
		return sum;
	}
	
	public static String izracunajSmer(NalogZaPlacanje nzp, String brojRacuna){
		if(nzp.getRacunDuznika().equals(brojRacuna)){
			return "Uplata";
		}
		else if(nzp.getRacunPoverioca().equals(brojRacuna)){
			return "Isplata";
		}
		return "Smer";
	}
	

}
