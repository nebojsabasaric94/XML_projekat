package com.xml.nalogzaplacanje;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.Date;
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
	public List<NalogZaPlacanje> findForIzvod(Date datum, String brojRacuna, int brStavkiPoPreseku,
			int redniBrojPreseka) {
		List<NalogZaPlacanje> tempList = new ArrayList<NalogZaPlacanje>();
		List<NalogZaPlacanje> outList = new ArrayList<NalogZaPlacanje>();

		List<NalogZaPlacanje> nalozi = (List<NalogZaPlacanje>) nalogZaPlacanjeRepo.findAll();
		for(int i=0; i<nalozi.size(); i++){
			if((nalozi.get(i).getRacunDuznika().equals(brojRacuna) || nalozi.get(i).getRacunPoverioca().equals(brojRacuna)) &&
					Math.abs(nalozi.get(i).datumNaloga.getTime()-datum.getTime())<100)
				tempList.add(nalozi.get(i));
		}
		System.out.println("RB PRESEKEA: "+redniBrojPreseka);
		for(int i=brStavkiPoPreseku*(redniBrojPreseka-1); i<(redniBrojPreseka-1)*brStavkiPoPreseku+brStavkiPoPreseku;i++ ){
			NalogZaPlacanje nzp = new NalogZaPlacanje();
			try{
				nzp = tempList.get(i);
			}
			catch(Exception l){
				break;
			}
			outList.add(nzp);
		}
		
		
		return outList;
	}

	@Override
	public double izracunajPredhodnoStanje(Date doDatuma, long doNaloga, String brRacuna) {
		double res = 0;
		List<NalogZaPlacanje> nalozi = (List<NalogZaPlacanje>) nalogZaPlacanjeRepo.findAll();
		for(int i=0; i<nalozi.size(); i++){
			if(nalozi.get(i).getId() >= doNaloga)
				break;
				if(nalozi.get(i).getRacunDuznika().equals(brRacuna))
					res -= nalozi.get(i).iznos.doubleValue();
				if(nalozi.get(i).getRacunPoverioca().equals(brRacuna))
					res += nalozi.get(i).iznos.doubleValue();
		}
		return res;
	}
	
	
}
