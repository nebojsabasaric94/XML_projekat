/*package bank.selfCertificate;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SelfCertificateServiceImpl implements SelfCertificateService{
	
	private final SelfCertificateRepo certificateRepository;
	
	@Autowired
	public SelfCertificateServiceImpl(final SelfCertificateRepo certificateRepository) {
		this.certificateRepository = certificateRepository;
	}

	@Override
	public List<SelfCertificate> findAll() {
		return certificateRepository.findAll();
	}

	@Override
	public SelfCertificate save(SelfCertificate certificate) {
		return certificateRepository.save(certificate);
	}

	@Override
	public SelfCertificate findOne(Long id) {
		return certificateRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		certificateRepository.delete(id);
	}

}
*/