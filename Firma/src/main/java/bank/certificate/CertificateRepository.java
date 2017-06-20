package bank.certificate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CertificateRepository extends CrudRepository<Certificate, Long>{

	@Query("select c from Certificate c where c.serialNumber like :serialNumber")
	public Certificate findBySerialNumber(@Param("serialNumber")String serialNumber);
	
}
