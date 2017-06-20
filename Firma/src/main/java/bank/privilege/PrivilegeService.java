package bank.privilege;

import java.util.List;

public interface PrivilegeService {
	
	public List<Privilege> findAll();
	
	public Privilege findOne(Long id);
	

}
