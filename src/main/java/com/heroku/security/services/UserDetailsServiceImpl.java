package com.heroku.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heroku.security.entities.Role;
import com.heroku.security.entities.UserAccount;
import com.heroku.security.entities.UserInputDto;
import com.heroku.security.repositories.UserRepository;
import com.heroku.security.repositories.UserRolesRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	UserRolesRepository userRolesRepository;

	public UserAccount findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void save(UserAccount user) {
		userRepository.save(user);
	}

	public List<Role> findRolesByUsername(String username) {
		return userRolesRepository.findUserRolesByUserName(username);
	}
	
	public void remove(String username){
		userRepository.delete(findByUsername(username));
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userRepository.findByUsername(username);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				grantedAuthorities);
	}

	public UserDetails addUser(UserInputDto user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());

		UserAccount u = findByUsername(user.getUsername());
		Set<Role> roles = new HashSet<Role>();
		for (String s : user.getRoles()){
			Role r = userRolesRepository.findByName(s);
			if (r == null)
				r = new Role(s);
			roles.add(r);
		}
		
		if (u == null) {
			u = new UserAccount(user.getUsername(), hashedPassword, user.getEmail(), roles);
		} else {
			u.setEmail(user.getEmail());
			u.setPassword(hashedPassword);
			u.setEnabled(1);
			u.setRoles(roles);
		}

		save(u);
		return loadUserByUsername(user.getUsername());
	}

}
