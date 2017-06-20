package com.example.bankXml.BankXml.firm;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FirmRepository extends CrudRepository<Firma, Long> {

	/*@Query("select f from Firma f where f.brojRacuna like :racun")
	Firma findByAccount(@Param("racun")String racun);
*/
}
