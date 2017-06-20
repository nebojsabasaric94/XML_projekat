package bank.nationalBank;

import java.util.List;

public interface NationalBankService {

	public NationalBank save(NationalBank nationalBank);
	
	public List<NationalBank> findAll();
	
	public NationalBank findCountry(Long id);
	
	public void delete(Long id);
}
