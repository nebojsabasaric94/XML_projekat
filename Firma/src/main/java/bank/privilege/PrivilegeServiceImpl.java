package bank.privilege;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {
	
	@Autowired
	private PrivilegeRepo repository;

	@Override
	public List<Privilege> findAll() {
		return (List<Privilege>) repository.findAll();
	}

	@Override
	public Privilege findOne(Long id) {
		return repository.findOne(id);
	}

}
