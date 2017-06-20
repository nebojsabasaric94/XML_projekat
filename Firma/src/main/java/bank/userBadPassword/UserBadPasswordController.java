package bank.userBadPassword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userBadPassword")
public class UserBadPasswordController {
	
	@Autowired
	private UserBadPasswordService userBadPasswordService;
	
	@PreAuthorize("hasAuthority('activateUser')")
	@GetMapping
	private List<UserBadPassword> findAll(){
		System.out.println(userBadPasswordService.findAll().size());
		return userBadPasswordService.findAll();
	}
	
	@PreAuthorize("hasAuthority('activateUser')")
	@DeleteMapping("/{id}")
	private void unblockUser(@PathVariable Long id){
		userBadPasswordService.delete(id);
	}

}
