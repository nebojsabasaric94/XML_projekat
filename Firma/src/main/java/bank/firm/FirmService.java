package bank.firm;

import java.util.List;

public interface FirmService {
	
	public List<Firma> findAll();

	public Firma findOne(Long id);
	
	public Firma findByName(String pib);

	public Firma save(Firma certificate);

}
