package bank.certificate;

public interface CertificateService {

	public Certificate findOnde(Long id);

	public Certificate save(Certificate certificate);
	
	public Certificate revoke(String serialNumber);
	
	public Certificate findBySerialNumber(String serialNumber);
}
