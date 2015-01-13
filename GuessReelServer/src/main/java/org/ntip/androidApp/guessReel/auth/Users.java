package org.ntip.androidApp.guessReel.auth;

import java.util.List;

import org.ntip.androidApp.guessReel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class Users implements UserDetailsService {

	private UserRepository repo;

	@Autowired
	public Users(UserRepository repo) {
		this.repo = repo;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		System.out.println("Inside loadUserByUsername:: username passed:" + username);
		List<GrantedAuthority> auth = AuthorityUtils
				.commaSeparatedStringToAuthorityList("USER");
		
		User user = repo.findByUsername(username);
		if (user == null) {
			System.out.println("Inside loadUserByUsername:: user is null");
			System.out.println("Inside loadUserByUsername:: hence new user");
		
			//System.out.println("Inside loadUserByUsername:: user is null");
			System.out.println("new User added::Username:" + username);
			
			//repo.save(new User(username,"pass"));
			//User newUser = repo.findByUsername(username);
			
			//return new org.springframework.security.core.userdetails.User(newUser.getUsername(), newUser.getPassword(),auth);
			return null;
		}
		
		
		/*if (username.equals("admin")) {*/
		if (user.getUsername().equals("admin")){
			System.out.println("Inside loadUserByUsername:: user is admin");
			repo.save(new User("admin","pass"));
			auth = AuthorityUtils
					.commaSeparatedStringToAuthorityList("ADMIN");
		}
		String password = user.getPassword();
		System.out.println("Inside loadUserByUsername:: username:" + username +"password::"+ password);
		return new org.springframework.security.core.userdetails.User(username, password,
				auth);
	}

}
