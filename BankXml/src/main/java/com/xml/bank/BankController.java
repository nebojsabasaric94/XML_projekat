package com.xml.bank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xml.client.BankClient;
import com.xml.mt102.GetMt102Response;
import com.xml.mt102.Mt102;
import com.xml.mt102.Mt102Service;

@RestController
@RequestMapping("/banka")
public class BankController {
	
	@Autowired
	private BankService bankService;
	
	@Autowired
	private Mt102Service mt102Service;
	
	/*@Autowired
	private FirmService firmService;*/
	
	@Autowired
	private BankClient bankClient;
	
	@GetMapping("/mt102/{bankId}")
	private void getMt102(@PathVariable Long bankId){
		Bank bank = bankService.findOne(bankId);
		List<Mt102> mt102List = bank.getMt102();
		for(int i = 0 ; i < mt102List.size(); i++){
			if(!mt102List.get(i).isObradjen()){
				GetMt102Response response = bankClient.sendMt102ToNationalBank(mt102List.get(i));
				if(response.getMt900().getIdPoruke().equals("MT900")){
					Mt102 mt102 = mt102List.get(i);
					mt102.setObradjen(true);
					
					mt102Service.save(mt102);
				}
			}
		}
	}

}
