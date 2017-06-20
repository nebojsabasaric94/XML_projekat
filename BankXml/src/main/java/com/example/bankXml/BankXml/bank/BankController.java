package com.example.bankXml.BankXml.bank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankXml.BankXml.client.BankClient;
import com.example.bankXml.BankXml.firm.FirmService;
import com.example.bankXml.BankXml.firm.Firma;
import com.example.bankXml.BankXml.mt102.GetMt102Response;
import com.example.bankXml.BankXml.mt102.Mt102;
import com.example.bankXml.BankXml.mt102.Mt102Service;

@RestController
@RequestMapping("/banka")
public class BankController {
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private Mt102Service mt102Service;
	
	@Autowired
	private FirmService firmService;
	
	@Autowired
	private BankClient bankClient;
	
	@GetMapping("/mt102/{bankId}")
	private void getMt102(@PathVariable Long bankId){
		Bank bank = bankService.findOne(bankId);
		List<Mt102> mt102List = bank.getMt102();
		for(int i = 0 ; i < mt102List.size(); i++){
			if(!mt102List.get(i).isObradjen()){
				GetMt102Response response = bankClient.sendMt102ToNationalBank(mt102List.get(i));
				Mt102 mt102 = mt102List.get(i);
				/*for(int j = 0 ; j < mt102.getNalogZaMT102().size(); j++){
					Firma firm = firmService.findByAccount(mt102.getNalogZaMT102().get(j).getRacunDuznika());
					firm.setStanjeRacuna(firm.getStanjeRacuna() - mt102.getNalogZaMT102().get(j).getIznos().intValueExact());
					firmService.save(firm);
				}*/
				//Firma firm = firmService.findByAccount(response.getMt900().getObracunskiRacunBankeDuznika());
				//firm.setStanjeRacuna(firm.getStanjeRacuna() - response.getMt900().getIznos().intValueExact());
				mt102.setObradjen(true);
				
				mt102Service.save(mt102);
			}
		}
	}

}
