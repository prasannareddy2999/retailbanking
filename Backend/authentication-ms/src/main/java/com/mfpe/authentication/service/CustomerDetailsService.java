package com.mfpe.authentication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mfpe.authentication.model.AppUser;
import com.mfpe.authentication.repository.UserRepository;

@Service
public class CustomerDetailsService implements UserDetailsService {

	// Class to Implement UserDetailsService in Spring security

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

//		AppUser user = null;
//		user = userRepo.findById(userid).get();
		
		Optional<AppUser> appUser = userRepo.findById(userid);

		if (appUser.isPresent()) {
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList("ROLE_" + appUser.get().getRole());
			return new User(appUser.get().getUserid(), appUser.get().getPassword(), grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("Username/Password is Invalid...Please Check");
		}
	}

}
