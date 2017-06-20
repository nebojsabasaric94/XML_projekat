package com.example.national_bank_xml.National_bank_xml.bank;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BankRepository extends CrudRepository<Bank, Long>{

	@Query("select b from Bank b where b.swiftKodBanke like :code")
	Bank findBySwiftCode(@Param("code")String code);

	
}
