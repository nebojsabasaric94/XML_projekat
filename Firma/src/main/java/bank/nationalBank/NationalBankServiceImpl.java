package bank.nationalBank;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NationalBankServiceImpl implements NationalBankService{
	
	private final NationalBankRepo nationalBankRepository;
	
	@Autowired
	public NationalBankServiceImpl(final NationalBankRepo nationalBankRepository) {
		this.nationalBankRepository = nationalBankRepository;
	}

	@Override
	public NationalBank save(NationalBank nationalBank) {
		return nationalBankRepository.save(nationalBank);
	}

	@Override
	public List<NationalBank> findAll() {
		return nationalBankRepository.findAll();
	}

	@Override
	public NationalBank findCountry(Long id) {
		return nationalBankRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		nationalBankRepository.delete(id);
	}

}
