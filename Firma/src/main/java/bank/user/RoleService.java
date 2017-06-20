package bank.user;

import java.util.List;

public interface RoleService {
	
	public List<Role> findAll();
	
	public Role findByEnumRole(EnumRole enumRole);

}
