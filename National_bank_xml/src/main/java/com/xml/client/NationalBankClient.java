package com.xml.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xml.mt102.GetMt910RequestMt102;
import com.xml.strukturartgsnaloga.GetMt910Request;
import com.xml.strukturartgsnaloga.GetMt910Response;
import com.xml.ws.WSTemplate;


@Component
public class NationalBankClient {

	@Autowired
	private WSTemplate webServiceTemplate;
	
	public GetMt910Response sendMt910(GetMt910Request request){
		//webServiceTemplate.setDefaultUri("");
		
		GetMt910Response response  = (GetMt910Response) webServiceTemplate.marshalSendAndReceive(request);
		return response;
	}
	
	public com.xml.mt102.GetMt910Response sendMt910mt102(GetMt910RequestMt102 request){
		
		com.xml.mt102.GetMt910Response response = (com.xml.mt102.GetMt910Response) webServiceTemplate.marshalSendAndReceive(request);
		
		return response;
	}
	
}
