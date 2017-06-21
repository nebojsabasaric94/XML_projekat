package com.xml.firm;

import org.springframework.data.repository.CrudRepository;

public interface FirmRepository extends CrudRepository<Firma, Long> {

	/*@Query("select f from Firma f where f.brojRacuna like :racun")
	Firma findByAccount(@Param("racun")String racun);
*/
}
