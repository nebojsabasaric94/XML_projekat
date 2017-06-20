package bank.certificate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService{

	@Autowired
	public CertificateRepository repository;
	@Override
	public Certificate save(Certificate certificate) {
		// TODO Auto-generated method stub
		return repository.save(certificate);
	}
	@Override
	public Certificate findOnde(Long id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}
	@Override
	public Certificate revoke(String serialNumber) {
		Certificate cer = repository.findBySerialNumber(serialNumber);
		cer.setPovucen(true);
		return repository.save(cer);
	}
	@Override
	public Certificate findBySerialNumber(String serialNumber) {
		// TODO Auto-generated method stub
		return repository.findBySerialNumber(serialNumber);
	}
	

}
